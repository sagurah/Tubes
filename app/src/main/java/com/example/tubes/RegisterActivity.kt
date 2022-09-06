package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var inputTanggal : TextInputLayout
    private lateinit var inputEmail : TextInputLayout
    private lateinit var inputNoTelp : TextInputLayout
    private lateinit var mainLayout : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputUsername = findViewById(R.id.registerLayoutUsername)
        inputPassword = findViewById(R.id.registerLayoutPassword)
        inputTanggal = findViewById(R.id.registerLayoutTanggalLahir)
        inputEmail = findViewById(R.id.registerLayoutEmail)
        inputNoTelp = findViewById(R.id.registerLayoutNoTelp)

        mainLayout = findViewById(R.id.registerLayout)
        val btnRegister : Button = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(View.OnClickListener{
            var checkRegistration = false
            val uname: String = inputUsername.getEditText()?.getText().toString()
            val pass: String = inputPassword.getEditText()?.getText().toString()
            val tgl: String = inputTanggal.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val noTelp: String = inputNoTelp.getEditText()?.getText().toString()

            val bundle = Bundle()

            if(uname.isEmpty()){
                inputUsername.setError("Username tidak boleh kosong")
                checkRegistration = false
            }

            if(pass.isEmpty()){
                inputPassword.setError("Password tidak boleh kosong")
                checkRegistration = false
            }

            if(tgl.isEmpty()){
                inputTanggal.setError("Tanggal Lahir tidak boleh kosong")
                checkRegistration = false
            }

            if(email.isEmpty()){
                inputEmail.setError("Email tidak boleh kosong")
                checkRegistration = false
            }

            if(noTelp.isEmpty()){
                inputNoTelp.setError("Nomor Telepon tidak boleh kosong")
                checkRegistration = false
            }

            if(
                !uname.isEmpty()
                && !pass.isEmpty()
                && !tgl.isEmpty()
                && !email.isEmpty()
                && !noTelp.isEmpty()
            ) checkRegistration = true

            if(checkRegistration)
                return@OnClickListener

            bundle.putString("username", uname)
            bundle.putString("password", pass)
            val moveLogin = Intent(this@RegisterActivity, MainActivity::class.java)
            moveLogin.putExtras(bundle)

            startActivity(moveLogin)
        })
    }
}