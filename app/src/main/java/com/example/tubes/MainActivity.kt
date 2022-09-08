package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var mainLayout : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLayout)

        setText()

        val btnRegister : Button = findViewById(R.id.btnRegis)
        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegistration = Intent(this, RegisterActivity::class.java)
            startActivity(moveRegistration)
        })

        val btnClear : Button = findViewById(R.id.btnClear)
        btnClear.setOnClickListener(View.OnClickListener{
            username = findViewById(R.id.textUsername)
            password = findViewById(R.id.textPassword)

            Snackbar.make(mainLayout, "Text Cleared Successfully", Snackbar.LENGTH_LONG).show()
            username.setText("")
            password.setText("")
        })

        val btnLogin : Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener{
            var checkLogin : Boolean
            username = findViewById(R.id.textUsername)
            password = findViewById(R.id.textPassword)
            val bundle = intent.extras

            var uname = username.text.toString()
            var pass = password.text.toString()

            if(uname.isEmpty())
                username.setError("Username must be filled with text")
                checkLogin = false

            if(pass.isEmpty())
                password.setError("Password must be filled with text")
                checkLogin = false

            if (bundle != null) {
                if(
                    uname == bundle.getString("username")
                    && pass == bundle.getString("password")
                ) checkLogin = true
            }

            if(!checkLogin){
                Snackbar.make(mainLayout, "Username atau Password anda tidak sesuai", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            else{
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
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