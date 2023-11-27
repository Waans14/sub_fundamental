package com.millenialzdev.githubusernavigationdanapi.ui

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.databinding.ItemCardviewUserBinding

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardviewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    class MyViewHolder(private val binding: ItemCardviewUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.tvUser.text = user.login
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivPhoto)
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("user", user.login)
                itemView.context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}