package com.example.tubes.dao

import androidx.room.*
import com.example.tubes.entity.User

@Dao
interface UserDAO {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getNotes() : List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
    suspend fun getNote(user_id: Int) : List<User>
}