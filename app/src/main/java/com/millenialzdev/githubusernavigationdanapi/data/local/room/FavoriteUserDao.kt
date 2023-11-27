package com.millenialzdev.githubusernavigationdanapi.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.millenialzdev.githubusernavigationdanapi.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavoriteUser)

    @Update
    fun update(user: FavoriteUser)

    @Delete
    fun delete(user: FavoriteUser)

    @Query("SELECT * from user ORDER BY id ASC")
    fun getAllUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("DELETE FROM user WHERE username = :username")
    fun deleteFavoriteUserByUsername(username: String)
}