package com.millenialzdev.githubusernavigationdanapi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.millenialzdev.githubusernavigationdanapi.R
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.UserResponse
import com.millenialzdev.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import com.millenialzdev.githubusernavigationdanapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        getAllUser()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(
            MainViewModelSetting::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.fav -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.set -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
                true
            }
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text

                    val user = searchView.text.toString()

                    findUser(user)

                    searchView.hide()
                    Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()

                    false
                }
        }

    }

    private fun findUser(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDataUser(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun getAllUser() {
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
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDataUser(users: List<ItemsItem>) {
        val userAdapter = UserAdapter()
        userAdapter.submitList(users)
        binding.rvUser.adapter = userAdapter
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}
