package com.abhishek.attendanceapp

import android.content.Context
import android.content.SharedPreferences

class PrefManager {
   lateinit var pref : SharedPreferences;
    lateinit var editor : SharedPreferences.Editor;
    lateinit var context : Context;

    var PRIVATE_MODE = 0;

    private val PREF_NAME = "attendance-welcome"

    private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
}