package com.example.tubes.entity

import com.example.tubes.room.jadwal.Jadwal
import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<Jadwal>
    )