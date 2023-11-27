package com.millenialzdev.githubusernavigationdanapi.data.remote.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.DetailUserRespone
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val userData = MutableLiveData<DetailUserRespone>()

    fun fetchUserData(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserRespone> {
            override fun onResponse(
                call: Call<DetailUserRespone>,
                response: Response<DetailUserRespone>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userData.postValue(responseBody)
                    }
                } else {
                    Log.e(TAG, "onResponse error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserRespone>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    // Metode untuk mendapatkan LiveData objek data yang akan ditampilkan di halaman DetailActivity
    fun getUserData(): LiveData<DetailUserRespone> {
        return userData
    }
}