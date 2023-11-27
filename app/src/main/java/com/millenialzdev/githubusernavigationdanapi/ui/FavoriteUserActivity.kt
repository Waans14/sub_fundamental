package com.millenialzdev.githubusernavigationdanapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import com.millenialzdev.githubusernavigationdanapi.databinding.ActivityFavoriteUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.*

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var viewModel: FavoriteUserViewModel

    companion object {
        private const val TAG = "FavoriteUserActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this@FavoriteUserActivity)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        getAllFavoriteUser()

    }

    private fun getAllFavoriteUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody : List<ItemsItem>? = response.body()
                    if (responseBody != null) {
                        setDataUser(responseBody)
                    }
                } else {
                    Log.e(FavoriteUserActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(FavoriteUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDataUser(users: List<ItemsItem>) {
        val userAdapter = UserAdapter()
        viewModel.getAllFavoriteUsers().observe(this) { favoriteUsers ->
            val items = favoriteUsers.map {
                it.username?.let { it1 -> it.avatarUrl?.let { it2 -> ItemsItem(login = it1, avatarUrl = it2) } }
            }
            userAdapter.submitList(items)
        }
        userAdapter.submitList(users)
        binding.rvFollow.adapter = userAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}