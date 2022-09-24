package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes.databinding.ActivityMainMenuBinding
import com.example.tubes.databinding.ActivityRegisterBinding
import com.example.tubes.room.UserDB
import com.example.tubes.room.user.User
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    var binding : ActivityRegisterBinding? = null
    val db by lazy { UserDB(this) }
    private val userDAO = db.UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moveLogin = Intent(this, MainActivity::class.java)
//        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener(View.OnClickListener{
            var checkRegistration = false
            val uname: String = binding?.registerLayoutUsername?.getEditText()?.getText().toString()
            val pass: String = binding?.registerLayoutPassword?.getEditText()?.getText().toString()
            val tgl: String = binding?.registerLayoutTanggalLahir?.getEditText()?.getText().toString()
            val email: String = binding?.registerLayoutEmail?.getEditText()?.getText().toString()
            val noTelp: String = binding?.registerLayoutNoTelp?.getEditText()?.getText().toString()

            val bundle = Bundle()

            CoroutineScope(Dispatchers.IO).launch {
                if(uname.isEmpty()){
                    binding?.registerLayoutUsername?.setError("Username tidak boleh kosong")
                    checkRegistration = false
                }

                if(pass.isEmpty()){
                    binding?.registerLayoutPassword?.setError("Password tidak boleh kosong")
                    checkRegistration = false
                }

                if(tgl.isEmpty()){
                    binding?.registerLayoutTanggalLahir?.setError("Tanggal Lahir tidak boleh kosong")
                    checkRegistration = false
                }

                if(email.isEmpty()){
                    binding?.registerLayoutEmail?.setError("Email tidak boleh kosong")
                    checkRegistration = false
                }

                if(noTelp.isEmpty()){
                    binding?.registerLayoutNoTelp?.setError("Nomor Telepon tidak boleh kosong")
                    checkRegistration = false
                }

                if(
                    !uname.isEmpty()
                    && !pass.isEmpty()
                    && !tgl.isEmpty()
                    && !email.isEmpty()
                    && !noTelp.isEmpty()
                ) checkRegistration = true

                // Store registration data after validation
                if(checkRegistration == true){
                    val user = User(0, uname, pass, tgl, email, noTelp)
                    userDAO.addUser(user)

                    bundle.putString("username", uname)
                    bundle.putString("password", pass)

                    moveLogin.putExtras(bundle)

                    startActivity(moveLogin)
                }

            }

            if(!checkRegistration)
                return@OnClickListener

        })
    }
}