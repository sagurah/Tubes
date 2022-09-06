package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    lateinit var getBundle : Bundle

    lateinit var vUname : String
    lateinit var vPass : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getBundle()
        setText()
    }

    fun getBundle(){
        getBundle = getIntent().getExtras()!!
        vUname = getBundle.getString("username")!!
        vPass = getBundle.getString("password")!!
    }

    fun setText(){
        username = findViewById(R.id.textUsername)
        password = findViewById(R.id.textPassword)
        username.setText(vUname)
        password.setText(vPass)
    }
}