package com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit

import com.millenialzdev.githubusernavigationdanapi.data.remote.response.DetailUserRespone
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Authorization: token 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")

    @GET("users")
    fun getAllUser(): Call<List<ItemsItem>>

    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserRespone>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}