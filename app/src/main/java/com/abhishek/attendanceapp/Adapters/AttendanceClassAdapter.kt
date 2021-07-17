package com.abhishek.attendanceapp.Adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.attendanceapp.Adapters.Classes.itemClass
import com.abhishek.attendanceapp.DateSelectActivity
import com.abhishek.attendanceapp.databinding.ItemClassBinding

class AttendanceClassAdapter(val list: ArrayList<itemClass>, val fragmentManager : FragmentManager) : RecyclerView.Adapter<AttendanceClassAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val bundle = Bundle()
            with(list[position]){
                binding.textClass.text = this.className
                bundle.putString("classId",this.className)
            }
            binding.root.setOnClickListener {
                val intent = Intent(it.context, DateSelectActivity::class.java)
                intent.putExtra("className", list[position].className);
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
