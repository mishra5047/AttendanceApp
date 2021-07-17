package com.abhishek.attendanceapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.attendanceapp.databinding.ActivityDateSelectBinding
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class DateSelectActivity : AppCompatActivity() {
    lateinit var binding : ActivityDateSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var flag = false;
        if(intent.hasExtra("action")){
            binding.btnTakeAttendance.setText("Get Report")
            flag = true;
        }

        var className = intent.getStringExtra("className")

        var date = ""
        binding.calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = dayOfMonth.toString() + "_" + (month + 1).toString() + "_" + year.toString()
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())


        binding.btnTakeAttendance.setOnClickListener {

            if(flag){
                val intent = Intent(this, ReportAttendance::class.java)
                intent.putExtra("className", className)
                if (date == "") {
                    date = currentDate
                }
                intent.putExtra("date", date)
                startActivity(intent)
            }else {
                val intent = Intent(this, StudentAttendanceActivity::class.java)
                intent.putExtra("className", className)
                if (date == "") {
                    date = currentDate
                }
                intent.putExtra("date", date)
                startActivity(intent)
            }
        }
    }
}