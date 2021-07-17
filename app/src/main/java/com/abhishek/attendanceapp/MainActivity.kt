package com.abhishek.attendanceapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.abhishek.attendanceapp.Fragments.ClassroomFragment
import com.abhishek.attendanceapp.Fragments.StatsFragment
import com.abhishek.attendanceapp.Fragments.StudentsFragment
import com.abhishek.attendanceapp.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadFragment(ClassroomFragment())

        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_classRoom, R.id.nav_students, R.id.nav_stats, R.id.repeatIntro
        )
            .setDrawerLayout(binding.drawerLayout)
            .build()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_classRoom -> {
                    loadFragment(ClassroomFragment())
                    binding.drawerLayout.close()
                    true
                }
                R.id.nav_students -> {
                    loadFragment(StudentsFragment())
                    binding.drawerLayout.close()
                    true
                }
                R.id.nav_stats -> {
                    loadFragment(StatsFragment())
                    binding.drawerLayout.close()
                    true;
                }
                R.id.repeatIntro -> {
                    prefManager!!.setFirstTimeLaunch(true)
                    startActivity(Intent(this, SplashScreenActivity::class.java));
                    true;
                }
                R.id.clearDatabase -> {
                    generateDialogue();
                    true;
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    private fun generateDialogue(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Clean Database")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to clear the whole database")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("")

            myRef.removeValue();
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        builder.setNegativeButton("No"){dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}