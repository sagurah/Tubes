package com.example.tubes

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tubes.main_fragment.HomeFragment
import com.example.tubes.main_fragment.ProfileFragment
import com.example.tubes.main_fragment.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenu : AppCompatActivity() {

    private lateinit var binding : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        replaceFragment(HomeFragment())
        binding = findViewById(R.id.bottomNavigationView)
        binding.setOnItemSelectedListener{ item->
            when(item.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
                R.id.plans -> replaceFragment(PlansFragment())
            }
            true
        }
    }
    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout) {
            val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainMenu)
            builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("YES", object : DialogInterface.OnClickListener{
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        finishAndRemoveTask()
                    }
                })
                .show()
            builder.setNegativeButton("NO", object : DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int){

                }
            })
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}

