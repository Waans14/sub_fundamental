package com.millenialzdev.githubusernavigationdanapi.data.remote.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    companion object {
        private const val TAG = "FollowersViewModel"
    }

    private val followers = MutableLiveData<List<ItemsItem>>()

    fun fetchUserData(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody : List<ItemsItem>? = response.body()
                    if (responseBody != null) {
                        followers.postValue(responseBody)
                    }
                } else {
                    Log.e(TAG, "onResponse error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowers(): LiveData<List<ItemsItem>> {
        return followers
    }
}