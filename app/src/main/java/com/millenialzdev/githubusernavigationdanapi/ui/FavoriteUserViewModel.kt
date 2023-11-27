package com.millenialzdev.githubusernavigationdanapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.millenialzdev.githubusernavigationdanapi.data.local.entity.FavoriteUser
import com.millenialzdev.githubusernavigationdanapi.data.repository.FavoriteUserRepository
import com.millenialzdev.githubusernavigationdanapi.data.repository.Result

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel() {
    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserRepository.insertFavoriteUser(favoriteUser)
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserRepository.deleteFavoriteUser(favoriteUser)
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserRepository.getAllFavoriteUsers()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserRepository.getFavoriteUserByUsername(username)
    }

    fun deleteFavoriteUserByUsername(username: String) {
        favoriteUserRepository.deleteFavoriteUserByUsername(username)
    }
}