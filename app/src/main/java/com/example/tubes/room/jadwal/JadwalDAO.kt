package com.example.tubes.room.jadwal

import androidx.room.*

@Dao
interface JadwalDAO {
    @Insert
    suspend fun addJadwal(jadwal: Jadwal)

    @Update
    suspend fun updateJadwal(jadwal: Jadwal)

    @Delete
    suspend fun deleteJadwal(jadwal: Jadwal)

    @Query("SELECT * FROM jadwal")
    suspend fun getJadwal(): List<Jadwal>

    @Query("SELECT * FROM jadwal WHERE id = :jadwal_id")
    suspend fun getJadwal(jadwal_id : Int): List<Jadwal>
}