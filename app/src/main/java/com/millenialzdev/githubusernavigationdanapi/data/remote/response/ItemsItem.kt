package com.millenialzdev.githubusernavigationdanapi.data.remote.response

import com.google.gson.annotations.SerializedName

data class ItemsItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

)