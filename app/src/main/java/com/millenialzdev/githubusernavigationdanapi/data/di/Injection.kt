package com.millenialzdev.githubusernavigationdanapi.data.di

import android.content.Context
import com.millenialzdev.githubusernavigationdanapi.data.local.room.FavoriteUserRoomDatabase
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import com.millenialzdev.githubusernavigationdanapi.data.repository.FavoriteUserRepository
import com.millenialzdev.githubusernavigationdanapi.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserRoomDatabase.getDatabase(context)
        val dao = database.favortieUserDao()
        val appExecutors = AppExecutors()
        return FavoriteUserRepository.getInstance(apiService, dao, appExecutors)
    }
}