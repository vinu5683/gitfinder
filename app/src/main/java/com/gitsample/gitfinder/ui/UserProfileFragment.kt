package com.gitsample.gitfinder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gitsample.gitfinder.R
import com.gitsample.gitfinder.databinding.FragmentUserProfileBinding
import com.gitsample.gitfinder.generics.ApiResult
import com.gitsample.gitfinder.ui.home.HomeViewModel

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val args: UserProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginId = args.loginId
        setUpViews()
        //User Profile Request
        viewModel.getUserProfile(loginId)
        subscribeToUserProfileData()
    }

    private fun subscribeToUserProfileData() {
        viewModel.currentUserProfile.observe(viewLifecycleOwner) { userDetail ->
            when (userDetail) {
                is ApiResult.Success -> {
                    binding.flLoadingView.visibility = View.GONE
                    Glide.with(binding.ivAvatar.context)
                        .load(userDetail.data.avatar)
                        .circleCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivAvatar)

                    binding.tvUsername.text = userDetail.data.userId
                    binding.tvBio.text = userDetail.data.bio ?: "No bio provided."
                    binding.tvFollowersCount.text = userDetail.data.followers.toString()
                    binding.tvReposCount.text = userDetail.data.publicRepos.toString()
                }

                is ApiResult.Error -> {
                    // no time to implement Error Screen
                }

                ApiResult.Loading -> {
                    binding.flLoadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpViews() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}