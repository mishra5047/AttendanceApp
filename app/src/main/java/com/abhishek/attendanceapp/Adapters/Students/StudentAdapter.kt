package com.abhishek.attendanceapp.Adapters.Students

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.attendanceapp.Fragments.ClassDetailFragment
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.Util.Validation
import com.abhishek.attendanceapp.databinding.AddStudentSpecificBinding
import com.abhishek.attendanceapp.databinding.EditStudentGeneralBinding
import com.abhishek.attendanceapp.databinding.ItemClassBinding
import com.abhishek.attendanceapp.databinding.ItemStudentBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class StudentAdapter(val list: ArrayList<itemStudent>, private val fragmentManager : FragmentManager) : RecyclerView.Adapter<StudentAdapter.ViewHolder>(){

     class ViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val bundle = Bundle()
            with(list[position]){
                binding.textStudent.text = this.studentName
                binding.textRollNo.text = this.studentRollNo
                bundle.putString("classId",this.className)
                bundle.putString("studentRollNo", this.studentRollNo)

                binding.editIcon.setOnClickListener {
                    editStudentDialogue(it, list[position])
                }

                binding.deleteIcon.setOnClickListener {
                    generateDeleteDialogue(it.context , list[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun editStudentDialogue(it : View, itemStudent: itemStudent){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(it.context)
        val factory = LayoutInflater.from(it.context)

        val view: View = factory.inflate(R.layout.edit_student_general, null)
        val binding = EditStudentGeneralBinding.bind(view)
        alertDialog.setView(binding.root)

        val d = alertDialog.create()
        d.show()

        var validation = Validation()
        var constant = Constants()

        //setting the data
        binding.className.setText(itemStudent.className)
        binding.studentName.setText(itemStudent.studentName)
        binding.studentRollNo.setText(itemStudent.studentRollNo)

        binding.btnAddStudent.setOnClickListener {

            if (!validation.checkEditText(binding.className)
                || !validation.checkEditText(binding.studentName)
                || !validation.checkEditText(binding.studentRollNo)
            ) {
                // Toasty.info(it.context, "In Valid").show()
            } else {
                //Toasty.info(it.context, "Valid").show()
                    val className = binding.className.text.toString()
                val studentName = binding.studentName.text.toString()
                val studentRollNo = binding.studentRollNo.text.toString()

                val database = FirebaseDatabase.getInstance()

                val pathDelete = constant.pathStudents + "/" + itemStudent.className + "/students/" + itemStudent.studentRollNo
                val deleteRef = database.getReference(pathDelete)
                deleteRef.removeValue()

                val itemNew = itemStudent(className, studentName, studentRollNo)
                val pathAdd = constant.pathStudents + "/" + className + "/students/" + studentRollNo
                val addRef = database.getReference(pathAdd)


                addRef.setValue(itemNew)
                Toast.makeText(it.context, "Student Edited", Toast.LENGTH_SHORT).show()
                val frag = ClassDetailFragment();
                val bundle = Bundle()
                bundle.putString("classId",itemNew.className)
                frag.arguments = bundle
                d.dismiss()
                loadFragment(frag)
            }
        }
    }

    private fun generateDeleteDialogue(context : Context, itemStudent: itemStudent){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Delete Student")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete " + itemStudent.studentName + " of " + itemStudent.className)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        var constant = Constants()

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            val stringPath = constant.pathStudents + "/" + itemStudent.className + "/students/" + itemStudent.studentRollNo

            val refDb = Firebase.database.getReference(stringPath)
            refDb.removeValue()
            Toasty.info(context, "Deleted Student's Record").show()
            val frag = ClassDetailFragment();
            val bundle = Bundle()
            bundle.putString("classId",itemStudent.className)
            frag.arguments = bundle
            loadFragment(frag)
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            fragmentManager
                .beginTransaction()
                .add(R.id.nav_host_fragment, fragment)
                .commit()
            return true
        }
        return false
    }
}
