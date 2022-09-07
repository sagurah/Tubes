package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tubes.databinding.ActivityMainBinding
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
}

