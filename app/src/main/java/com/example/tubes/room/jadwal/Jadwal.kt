package com.example.tubes.room.jadwal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Jadwal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namaTrainer: String,
    val jamMulai: String,
    val jamAkhir: String,
    val plan: String
)
