package com.example.tubes.entity

import com.example.tubes.models.Plans
import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("status") val stt:String,
    val totaldata: Int,
    val data:List<Plans>
    )