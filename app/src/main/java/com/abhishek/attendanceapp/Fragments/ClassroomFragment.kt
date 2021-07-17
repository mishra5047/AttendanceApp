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
import com.abhishek.attendanceapp.Adapters.Classes.ClassAdapter
import com.abhishek.attendanceapp.Adapters.Classes.itemClass
import com.abhishek.attendanceapp.Adapters.Students.itemStudent
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.Util.Validation
import com.abhishek.attendanceapp.databinding.AddClassroomBinding
import com.abhishek.attendanceapp.databinding.AddStudentSpecificBinding
import com.abhishek.attendanceapp.databinding.FragmentClassDetailsBinding
import com.abhishek.attendanceapp.databinding.FragmentClassroomBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class ClassroomFragment : Fragment() {
    lateinit var binding : FragmentClassroomBinding

    // Role -> attendance lena for all the students

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassroomBinding.inflate(inflater, container, false)

        container!!.removeAllViews();
        setViews()
        return binding.root
    }

    private fun setViews() {
        getDataFromFirebase();

        binding.addClassroom.setOnClickListener {
            //function to add classroom
            createDialogue(it)
        }

    }

    private fun getDataFromFirebase() {
        var constant = Constants()
        val path = constant.pathStudents;

        val database = Firebase.database
        val dataFetch = database.getReference(path)

        // Read from the database
        dataFetch.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val listClass : ArrayList<itemClass> = ArrayList<itemClass>()
                for(snapShot : DataSnapshot in snapshot.children){
                    listClass.add(itemClass(snapShot.key.toString()));
                }
                //Toasty.info(binding.root.context, "size = " + listClass.size).show()

                if(listClass.size == 0){
                    binding.recClass.visibility = View.GONE
                    binding.imageNothing.visibility = View.VISIBLE

                    Toasty.info(binding.root.context, "No classrooms found, add using + button below").show()
                    return
                }
                binding.imageNothing.visibility = View.GONE
                binding.recClass.visibility = View.VISIBLE

                binding.recClass.adapter = ClassAdapter(listClass, fragmentManager!!,0);
                binding.recClass.layoutManager = GridLayoutManager(context, 2)
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Firebase Error Found"
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    fun createDialogue(it : View) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(it.context)
        val factory = LayoutInflater.from(it.context)

        val view: View = factory.inflate(R.layout.add_classroom, null)
        val binding = AddClassroomBinding.bind(view)
        alertDialog.setView(binding.root)

        val d = alertDialog.create()
        d.show();

        var validation = Validation()
        var constant = Constants()

        binding.btnAddStudent.setOnClickListener {

            if (!validation.checkEditText(binding.className)
            ) {
                // Toasty.info(it.context, "In Valid").show()
            } else {
                //Toasty.info(it.context, "Valid").show()
                val className = binding.className.text.toString();
                val path = constant.pathStudents + "/" + className + "/students";
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference(path)
                val itemStudent = itemStudent(className, "temp", "999")
                myRef.child("999").setValue(itemStudent)
                Toast.makeText(it.context, "Class Added with a Temporary Student", Toast.LENGTH_SHORT).show();
                d.dismiss();
                getDataFromFirebase();
            }
        }
    }
}