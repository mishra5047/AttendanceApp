package com.abhishek.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.attendanceapp.Adapters.StudentAttendanceAdapter
import com.abhishek.attendanceapp.Adapters.Students.StudentAdapter
import com.abhishek.attendanceapp.Adapters.Students.itemStudent
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.databinding.ActivityStudentAttendanceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class StudentAttendanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentAttendanceBinding
    lateinit var className: String;
    lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        className = intent.getStringExtra("className").toString()
        date = intent.getStringExtra("date").toString();

        setViews()
    }

    private fun setViews(){
        binding.text.text = "Students in " + className;
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        var constant = Constants()
        val path = constant.pathStudents + "/" + className + "/students";

        val pathCheck = constant.pathStudents + "/$className"+ "/classes" + "/$date"
        val checkIfExists = Firebase.database.getReference(pathCheck)

        checkIfExists.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    //attendance already exists
                    Toasty.info(applicationContext, "Attendance already taken for " + className + " on " + date).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java));
                }else{
                    fetchStudentsList(path)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Firebase Error Found"
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

    }

    fun fetchStudentsList(path : String){
        Toasty.info(applicationContext, "Select The CheckBox For Present Students").show()

        val database = Firebase.database
        val dataFetch = database.getReference(path)

        // Read from the database
        dataFetch.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val listStudent = ArrayList<itemStudent>();

                for(snapShot : DataSnapshot in snapshot.children){
                    val obj = snapShot.getValue(itemStudent::class.java)
                    listStudent.add(obj!!);
                }
                //to check whether the list is empty or not
                var flag = false;
                if(listStudent.size == 0){
                    flag = true;
                    binding.imageNothing.visibility = View.VISIBLE
                    binding.recStudents.visibility = View.GONE
                }

                val adapter = StudentAttendanceAdapter(listStudent);
                binding.recStudents.adapter = adapter
                binding.recStudents.layoutManager = LinearLayoutManager(applicationContext)

                binding.btnTakeAttendance.setOnClickListener {

                    if(flag){
                        //no students
                        Toasty.error(it.context, "No students found, add students from the students tab").show();
                        startActivity(Intent(it.context, MainActivity::class.java));
                        return@setOnClickListener;
                    }

                    val presentList : HashSet<itemStudent> = adapter.presentList;
                    val absentList : HashSet<itemStudent> = adapter.absentList;

                    //Toasty.info(it.context, presentList.size.toString() + " & " + absentList.size.toString()).show();
                    Toasty.info(it.context, "Uploading Attendance").show();
                    val pathAbsent = Constants().pathStudents + "/" + className + "/classes/" + date + "/absent";
                    val pathPresent = Constants().pathStudents + "/" + className + "/classes/" + date + "/present";

                    val refAbsent = Firebase.database.getReference(pathAbsent)
                    val refPresent = Firebase.database.getReference(pathPresent)

                    var i = 0;
                    for(itemStudent in absentList){
                        refAbsent.child((i++).toString()).setValue(itemStudent.studentRollNo + "-" + itemStudent.studentName)
                    }
                    i = 0;
                    for(itemStudent in presentList){
                        refPresent.child((i++).toString()).setValue(itemStudent.studentRollNo + "-" + itemStudent.studentName)
                    }

                    Toasty.success(it.context, "Attendance Uploaded").show();

                    startActivity(Intent(applicationContext, MainActivity::class.java));
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Firebase Error Found"
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

}