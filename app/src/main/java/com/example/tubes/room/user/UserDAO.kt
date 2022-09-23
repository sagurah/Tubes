package com.example.tubes.room.user

import androidx.room.*

@Dao
interface UserDAO {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUser() : List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
    suspend fun getUser(user_id: Int) : List<User>

    @Query("SELECT * FROM user WHERE username =:username AND password =:password")
    suspend fun getUserByCreds(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE username =:username AND email =:email")
    suspend fun getUserByUsernameEmail(username: String, email: String): User?

}