package com.abhishek.attendanceapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.abhishek.attendanceapp.Adapters.Classes.ClassAdapter
import com.abhishek.attendanceapp.Adapters.Classes.itemClass
import com.abhishek.attendanceapp.R
import com.abhishek.attendanceapp.ReportAttendance
import com.abhishek.attendanceapp.StudentAttendanceActivity
import com.abhishek.attendanceapp.Util.Constants
import com.abhishek.attendanceapp.databinding.FragmentClassroomBinding
import com.abhishek.attendanceapp.databinding.FragmentStatsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class StatsFragment : Fragment() {
    lateinit var binding : FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        container!!.removeAllViews();

        binding = FragmentStatsBinding.inflate(inflater, container, false)
        setViews()
        return binding.root
    }

    private fun setViews() {
        getDataFromFirebase();

        binding.addClassroom.setOnClickListener {
            Toasty.info(it.context, "Add Class in either Classroom or Student Section").show()
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

                if(listClass.size == 0){
                    binding.recClass.visibility = View.GONE
                    binding.imageNothing.visibility = View.VISIBLE

                    Toasty.info(binding.root.context, "No classrooms found, add using + button below").show()
                    return
                }
                binding.imageNothing.visibility = View.GONE
                binding.recClass.visibility = View.VISIBLE
                binding.recClass.adapter = ClassAdapter(listClass, fragmentManager!!,2);
                binding.recClass.layoutManager = GridLayoutManager(context, 2)
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Firebase Error Found"
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }
}