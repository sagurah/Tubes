package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler



class FTOActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = getSharedPreferences("prefs", 0)
        val firstRun = settings.getBoolean("firstRun", false)

        if (firstRun == true)//if running for the first time
        //Splash will load for the first time
        {
            val editor = settings.edit()
            editor.putBoolean("firstRun", true)
            editor.commit()

            setContentView(R.layout.activity_ftoactivity)

            Handler().postDelayed(Runnable{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },4000)
        }else{
        val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}




