package com.abhishek.attendanceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.attendanceapp.Adapters.Attedance.StudentAttendanceAdapter
import com.abhishek.attendanceapp.Adapters.Classes.itemClass
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.databinding.ActivityReportAttendanceBinding
import com.abhishek.attendanceapp.databinding.ActivityStudentAttendanceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class ReportAttendance : AppCompatActivity() {
    lateinit var binding: ActivityReportAttendanceBinding
    lateinit var reference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViews();
    }

    fun setViews(){

        getTotalStudents();
    }

    private fun getTotalStudents(){
        val className = intent.getStringExtra("className").toString();
        val date = intent.getStringExtra("date").toString();

        val constant = Constants()
        val path = constant.pathStudents + "/" + className + "/classes/" + date
        reference = Firebase.database.getReference(path);

        reference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    getPresentAndAbsentFromFirebase()
                }else{
                    displayNoClass(className, date)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Error";
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun displayNoClass(className : String, date : String){
        Toasty.error(applicationContext, "There were no class for " + className + " on " + date).show()
        binding.imageNothing.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE

        Handler().postDelayed({
            onBackPressed();
        }, 5000)
    }

    fun getPresentAndAbsentFromFirebase(){
        reference.child("present").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                binding.presentText.text = snapshot.childrenCount.toString()

                val listPresent = ArrayList<String>()
                for(snapShot : DataSnapshot in snapshot.children){
                    listPresent.add(snapShot.value.toString());
                }
                binding.recyclerPresent.adapter = StudentAttendanceAdapter(listPresent);
                binding.recyclerPresent.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Error";
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        reference.child("absent").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                binding.absentText.text = snapshot.childrenCount.toString()

                val listAbsent = ArrayList<String>()
                for(snapShot : DataSnapshot in snapshot.children){
                    listAbsent.add(snapShot.value.toString());
                }
                binding.recyclerAbsent.adapter = StudentAttendanceAdapter(listAbsent);
                binding.recyclerAbsent.layoutManager = LinearLayoutManager(applicationContext)

            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Error";
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}