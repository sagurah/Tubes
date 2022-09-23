package com.example.tubes.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val username  : String,
    val password  : String,
    val tglLahir  : String,
    val email     : String,
    val nomorTelp : String,

)