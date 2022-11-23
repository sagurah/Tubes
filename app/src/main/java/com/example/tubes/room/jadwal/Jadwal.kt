package com.example.tubes.room.jadwal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Jadwal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("nama_trainer")
    val namaTrainer: String,
    @SerializedName("jam_mulai")
    val jamMulai: String,
    @SerializedName("jam_akhir")
    val jamAkhir: String,
    val plan: String
)
