package com.millenialzdev.githubusernavigationdanapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.millenialzdev.githubusernavigationdanapi.R
import com.millenialzdev.githubusernavigationdanapi.data.local.entity.FavoriteUser
import com.millenialzdev.githubusernavigationdanapi.data.remote.viewModel.DetailViewModel
import com.millenialzdev.githubusernavigationdanapi.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    private var favoriteUser: FavoriteUser? = null
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelProvider.NewInstanceFactory()
        viewModel = factory.create(DetailViewModel::class.java)
        favoriteUserViewModel = obtainViewModel(this@DetailActivity)

        val userLogin = intent.getStringExtra("user")

        if (userLogin != null) {
            viewModel.fetchUserData(userLogin)
        }


        favoriteUser = FavoriteUser()

        viewModel.getUserData().observe(this) { user ->

            val followers = user.followers.toString() + " Followers"
            val following = user.following.toString() + " Following"

            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.ivPhoto)
            binding.tvUser.text = user.login
            binding.tvNama.text = user.name
            binding.tvFollowers.text = followers
            binding.tvFollowing.text = following

            binding.progressBar.visibility = View.GONE

            favoriteUser?.avatarUrl = user.avatarUrl
            favoriteUser?.username = user.login

        }

        adapter = PagerAdapter(this)
        adapter.username = userLogin ?: ""

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Followers"
                1 -> tab.text = "Following"
            }
        }.attach()

        if (userLogin != null) {
            favoriteUserViewModel.getFavoriteUserByUsername(userLogin).observe(this, Observer { favoriteUserFromDb ->
                if (favoriteUserFromDb == null) {
                    binding.fab.setImageResource(R.drawable.baseline_favorite_border_24)
                    binding.fab.setOnClickListener {
                        favoriteUserViewModel.insertFavoriteUser(favoriteUser as FavoriteUser)
                        binding.fab.setImageResource(R.drawable.baseline_favorite_24)
                    }
                } else {
                    binding.fab.setImageResource(R.drawable.baseline_favorite_24)
                    binding.fab.setOnClickListener {
                        favoriteUserViewModel.deleteFavoriteUserByUsername(userLogin)
                        binding.fab.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                }
            })
        }



    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }
}