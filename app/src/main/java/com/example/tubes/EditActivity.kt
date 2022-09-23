package com.example.tubes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tubes.room.UserDB
import com.example.tubes.*
import com.example.tubes.databinding.ActivityEditBinding
import com.example.tubes.databinding.FragmentProfileBinding
import com.example.tubes.room.user.User
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences : SharedPreferences? = null
    private var bind: ActivityEditBinding? = null
    private val binding get() = bind!!

    val db by lazy { UserDB(this) }
    private val userDAO = db.UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        btnEditProfile.setOnClickListener(){
            val username: String = binding?.tilUsername?.getEditText()?.getText().toString()
            val password: String = binding?.tilPassword?.getEditText()?.getText().toString()
            val email: String = binding?.tilEmail?.getEditText()?.getText().toString()
            val tglLahir: String = binding?.tilTglLahir?.getEditText()?.getText().toString()
            val noTelp: String = binding?.tilNoTelp?.getEditText()?.getText().toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = User(0, username, password, tglLahir, email, noTelp)
                userDAO.addUser(user)
            }
        }
    }

}