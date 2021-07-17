package com.abhishek.attendanceapp.Adapters.Classes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.attendanceapp.DateSelectActivity
import com.abhishek.attendanceapp.Fragments.ClassDetailFragment
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.StudentAttendanceActivity
import com.abhishek.attendanceapp.databinding.ItemClassBinding

class ClassAdapter(val list: ArrayList<itemClass>, val fragmentManager : FragmentManager, var mode: Int) : RecyclerView.Adapter<ClassAdapter.ViewHolder>(){
    // 0 -> classroom attendance
    // 1 -> students lists
    // 2 -> class attendance report

    inner class ViewHolder(val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val bundle = Bundle()
            with(list[position]) {
                binding.textClass.text = this.className
                bundle.putString("classId", this.className)

                binding.root.setOnClickListener {
                    if (mode == 0 || mode == 2) {
                        val intent = Intent(it.context, DateSelectActivity::class.java)
                        intent.putExtra("className", this.className)
                        if(mode == 2)
                            intent.putExtra("action", "report");

                        it.context.startActivity(intent)
                    } else {
                        val frag = ClassDetailFragment();
                        frag.arguments = bundle
                        loadFragment(frag)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            fragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
            return true
        }
        return false
    }
}
