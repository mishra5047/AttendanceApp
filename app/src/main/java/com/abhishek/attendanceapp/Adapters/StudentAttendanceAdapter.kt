package com.abhishek.attendanceapp.Adapters

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
import com.abhishek.attendanceapp.Adapters.Students.itemStudent
import com.abhishek.attendanceapp.Fragments.ClassDetailFragment
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.Util.Validation
import com.abhishek.attendanceapp.databinding.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty

class StudentAttendanceAdapter(val list: ArrayList<itemStudent>) : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>(){

    val presentList = HashSet<itemStudent>();
    val absentList = HashSet<itemStudent>();

    class ViewHolder(val binding: ItemStudentAttendanceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.textStudent.text = this.studentName
                binding.textRollNo.text = this.studentRollNo

                absentList.add(list[position])

                binding.root.setOnClickListener {
                    if (!binding.checkBox.isChecked) {
                        binding.checkBox.isChecked = true;
                        presentList.add(list[position])
                        absentList.remove(list[position])
                    }else{
                        binding.checkBox.isChecked = false;
                        absentList.add(list[position])
                        presentList.remove(list[position])
                    }
                }


                binding.checkBox.setOnClickListener {
                    if (binding.checkBox.isChecked){
                        presentList.add(list[position])
                        absentList.remove(list[position])
                    }else{
                        absentList.add(list[position])
                        presentList.remove(list[position])
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun returnPresentList() : HashSet<itemStudent>{
        return presentList;
    }

    fun returnAbsentList() : HashSet<itemStudent>{
        return absentList;
    }

}
