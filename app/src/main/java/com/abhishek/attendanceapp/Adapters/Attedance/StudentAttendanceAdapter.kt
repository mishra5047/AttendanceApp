package com.abhishek.attendanceapp.Adapters.Attedance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.attendanceapp.databinding.*

class StudentAttendanceAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemStudentAttendanceDisplayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentAttendanceDisplayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                val string = this.split("-")

                binding.textStudent.text = string[1]
                binding.textRollNo.text = string[0]
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
