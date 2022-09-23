package com.example.tubes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tubes.room.jadwal.Jadwal
import com.example.tubes.room.jadwal.JadwalDAO

@Database(
    entities = [Jadwal::class],
    version = 1
)

abstract class JadwalDB: RoomDatabase(){
    abstract fun JadwalDAO() : com.example.tubes.room.jadwal.JadwalDAO
    companion object {
        @Volatile private var instance: JadwalDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                JadwalDB::class.java, "jadwalDatabases.db"
            ).build()
    }
}
