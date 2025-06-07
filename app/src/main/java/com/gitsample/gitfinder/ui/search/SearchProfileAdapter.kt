package com.gitsample.gitfinder.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.databinding.ItemLayoutProfileListBinding


class SearchProfileAdapter(private val onAdapterActionListener: OnAdapterActionListener) :
    PagingDataAdapter<UserItem, SearchProfileAdapter.ProfileViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemLayoutProfileListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ProfileViewHolder(
        private val binding: ItemLayoutProfileListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem) {
            binding.tvProfileName.text = user.login
            binding.tvProfileType.text = user.type
            Glide.with(binding.ivProfileAvatar.context)
                .load(user.avatar)
                .circleCrop()
                .placeholder(com.gitsample.gitfinder.R.drawable.ic_launcher_foreground)
                .into(binding.ivProfileAvatar)
            binding.itemParentLayout.setOnClickListener {
                onAdapterActionListener.onClick(user)
            }
            binding.imgBookmark.setOnClickListener{
                //onAdapterActionListener.onBookmarkIconClick(user.login)
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}