package com.abhishek.attendanceapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.attendanceapp.Adapters.Classes.ClassAdapter
import com.abhishek.attendanceapp.Adapters.Students.StudentAdapter
import com.abhishek.attendanceapp.Adapters.Students.itemStudent
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.Util.Validation
import com.abhishek.attendanceapp.databinding.AddStudentGeneralBinding
import com.abhishek.attendanceapp.databinding.AddStudentSpecificBinding
import com.abhishek.attendanceapp.databinding.FragmentClassDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class ClassDetailFragment : Fragment() {
    lateinit var binding : FragmentClassDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassDetailsBinding.inflate(inflater, container, false)

        container!!.removeAllViews();
        setViews()
        return binding.root
    }

    private fun setViews() {
        val classStud = arguments?.getString("classId");
        binding.text.text = "Students in class " + classStud;

        getDataFromFirebase(classStud!!)

        //add student dialog
        binding.addStudent.setOnClickListener {
            createDialogue(it, classStud)
        }
    }

    private fun getDataFromFirebase(classId : String) {
        var constant = Constants()
        val path = constant.pathStudents + "/" + classId + "/students";

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

                if(listStudent.size == 0){
                    binding.recClass.visibility = View.GONE
                    binding.imageNone.visibility = View.VISIBLE
                }else {
                    binding.imageNone.visibility = View.GONE
                    binding.recClass.visibility = View.VISIBLE

                    binding.recClass.adapter = StudentAdapter(listStudent, fragmentManager!!);
                    binding.recClass.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Firebase Error Found"
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    fun createDialogue(it : View, className : String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(it.context)
        val factory = LayoutInflater.from(it.context)

        val view: View = factory.inflate(R.layout.add_student_specific, null)
        val binding = AddStudentSpecificBinding.bind(view)
        alertDialog.setView(binding.root)

        val d = alertDialog.create()
        d.show();

        var validation = Validation()
        var constant = Constants()

        binding.btnAddStudent.setOnClickListener {

            if (!validation.checkEditText(binding.studentName)
                || !validation.checkEditText(binding.studentRollNo)
            ) {
                // Toasty.info(it.context, "In Valid").show()
            } else {
                //Toasty.info(it.context, "Valid").show()
                val studentName = binding.studentName.text.toString()
                val studentRollNo = binding.studentRollNo.text.toString()

                val itemStudent = itemStudent(className, studentName, studentRollNo)
                val path = constant.pathStudents + "/" + className + "/students/" + studentRollNo
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference(path)
                myRef.setValue(itemStudent)
                Toast.makeText(it.context, "Student Added", Toast.LENGTH_SHORT).show();
                d.dismiss();
                getDataFromFirebase(className)
            }
        }
    }

}