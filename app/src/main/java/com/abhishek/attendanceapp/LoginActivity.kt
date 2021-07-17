package com.abhishek.attendanceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.attendanceapp.databinding.ActivityLoginBinding
import com.abhishek.attendanceapp.databinding.ActivitySplashScreenBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

        }
    }
}