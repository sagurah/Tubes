package com.example.tubes.main_fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tubes.*
import com.example.tubes.databinding.FragmentProfileBinding
import com.example.tubes.room.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    val db by lazy { UserDB(requireContext())}
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences: SharedPreferences? = null
    private var bind: FragmentProfileBinding? = null
    private val binding get() = bind!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences(preference, Context.MODE_PRIVATE)

        CoroutineScope(Dispatchers.IO).launch {
            val user = db.UserDAO().getUser(sharedPreferences!!.getString(id, "")!!.toInt())?.get(0)
            binding.tvUsername.setText(user?.username)
            binding.tvEmail.setText(user?.email)
            binding.tvNomorTelp.setText(user?.nomorTelp)
            binding.tvTanggalLahir.setText(user?.tglLahir)

            binding.btnEdit.setOnClickListener{
                val move = Intent(activity, EditActivity::class.java)
                startActivity(move)
                activity?.finish()
            }
        }

    }
}