package com.abhishek.attendanceapp.Util

import android.widget.EditText

class Validation {
    fun checkEditText(editText: EditText): Boolean {
        val str = editText.text.toString();
        if(str.isEmpty()){
            editText.error = "Can't Be Empty"
            return false;
        }
        return true;
    }
}