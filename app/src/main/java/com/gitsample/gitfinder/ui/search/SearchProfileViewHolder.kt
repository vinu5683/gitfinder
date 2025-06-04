package com.gitsample.gitfinder.ui.search

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gitsample.gitfinder.R
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.databinding.ItemLayoutFooterBinding
import com.gitsample.gitfinder.databinding.ItemLayoutProfileListBinding

class SearchProfileViewHolder(private val binding: ItemLayoutProfileListBinding) :
    ViewHolder(binding.root) {
    fun bind(user: UserItem) {
        binding.apply {
            tvProfileName.text = user.login
            tvProfileType.text = user.type
            Glide.with(itemView.context)
                .load(user.avatar)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivProfileAvatar)
        }
    }
}

class LoadingViewHolder(binding: ItemLayoutFooterBinding) : ViewHolder(binding.root)