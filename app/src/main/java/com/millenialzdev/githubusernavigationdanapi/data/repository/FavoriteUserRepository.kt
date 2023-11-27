package com.millenialzdev.githubusernavigationdanapi.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.millenialzdev.githubusernavigationdanapi.data.local.entity.FavoriteUser
import com.millenialzdev.githubusernavigationdanapi.data.local.room.FavoriteUserDao
import com.millenialzdev.githubusernavigationdanapi.data.local.room.FavoriteUserRoomDatabase
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiService
import com.millenialzdev.githubusernavigationdanapi.utils.AppExecutors
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<FavoriteUser>>>()

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getAllUser()
    }

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute {
            favoriteUserDao.insert(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute {
            favoriteUserDao.delete(favoriteUser)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    fun deleteFavoriteUserByUsername(username: String) {
        appExecutors.diskIO.execute {
            favoriteUserDao.deleteFavoriteUserByUsername(username)
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            appExecutors: AppExecutors
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(apiService, favoriteUserDao, appExecutors)
            }.also { instance = it }
    }
}