package com.example.tubes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.tubes.room.UserDB
import com.example.tubes.*
import com.example.tubes.databinding.ActivityEditBinding
import com.example.tubes.databinding.FragmentProfileBinding
import com.example.tubes.main_fragment.ProfileFragment
import com.example.tubes.room.user.User
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences : SharedPreferences? = null
//    private var bind: ActivityEditBinding? = null
//    private val binding get() = bind!!

    val db by lazy { UserDB(this) }
    private val userDAO = db.UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        btnEditProfile.setOnClickListener(){
            val username: String = findViewById<View?>(R.id.tilUsername).toString()
            val password: String = findViewById<View?>(R.id.tilPassword).toString()
            val email: String = findViewById<View?>(R.id.tilEmail).toString()
            val tglLahir: String = findViewById<View?>(R.id.tilTglLahir).toString()
            val noTelp: String = findViewById<View?>(R.id.tilNoTelp).toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = User(0, username, password, tglLahir, email, noTelp)
                userDAO.addUser(user)

                replaceFragment(ProfileFragment())
            }
        }

    }
    fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}