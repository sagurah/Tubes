package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setText()

        val btnRegister : Button = findViewById(R.id.btnRegis)
        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegistration = Intent(this, RegisterActivity::class.java)
            startActivity(moveRegistration)
        })
    }

    fun setText() {

        username = findViewById(R.id.textUsername)
        password = findViewById(R.id.textPassword)

        val bundle = intent.extras
        if (bundle != null) {
            username.setText(bundle.getString("username"))
        }
    }
}