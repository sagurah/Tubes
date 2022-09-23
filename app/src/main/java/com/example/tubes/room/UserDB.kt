package com.example.tubes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tubes.room.user.User
import com.example.tubes.room.user.UserDAO

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDB: RoomDatabase() {
    abstract fun UserDAO() : com.example.tubes.room.user.UserDAO
    companion object {
        @Volatile private var instance : UserDB? = null
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
                UserDB::class.java,
                "userDatabases.db"
            ).build()
    }
}