package com.millenialzdev.githubusernavigationdanapi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.millenialzdev.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.millenialzdev.githubusernavigationdanapi.data.remote.viewModel.FollowersViewModel
import com.millenialzdev.githubusernavigationdanapi.data.remote.viewModel.FollowingViewModel
import com.millenialzdev.githubusernavigationdanapi.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private var position: Int = 0
    private var username: String? = null

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFollowers: FollowersViewModel
    private lateinit var viewModelFollowing: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        viewModelFollowers = ViewModelProvider(this)[FollowersViewModel::class.java]
        viewModelFollowing = ViewModelProvider(this)[FollowingViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        binding.progressBar.visibility = View.VISIBLE

        if (position == 1) {
            if (username != null) {
                viewModelFollowers.fetchUserData(username.toString())

                viewModelFollowers.getFollowers().observe(requireActivity()) { followers ->
                    setDataFollow(followers)
                    binding.progressBar.visibility = View.GONE
                }
            }
        } else {
            if (username != null) {
                viewModelFollowing.fetchUserData(username.toString())

                viewModelFollowing.getFollowing().observe(requireActivity()) { following ->
                    setDataFollow(following)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun setDataFollow(follow: List<ItemsItem>) {
        val followAdapter = FollowAdapter()
        followAdapter.submitList(follow)
        binding.rvFollow.adapter = followAdapter
    }

    companion object {

        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(position: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
