package com.gitsample.gitfinder.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.databinding.ItemLayoutFooterBinding
import com.gitsample.gitfinder.databinding.ItemLayoutProfileListBinding
import com.gitsample.gitfinder.ui.home.HomeFragment.Companion.TYPE_ITEM
import com.gitsample.gitfinder.ui.home.HomeFragment.Companion.TYPE_LOADING_FOOTER


class SearchProfileAdapter() :
    RecyclerView.Adapter<ViewHolder>() {
    private val searchResult = mutableListOf<UserItem>()
    private var showLoadingFooter = false

    fun submitList(newList: List<UserItem>) {
        searchResult.clear()
        searchResult.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding: ItemLayoutProfileListBinding =
                ItemLayoutProfileListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SearchProfileViewHolder(binding)
        } else {
            val binding: ItemLayoutFooterBinding =
                ItemLayoutFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SearchProfileViewHolder -> {
                holder.bind(searchResult[position])
            }

            is LoadingViewHolder -> {}
        }
    }

    override fun getItemCount(): Int {
        return searchResult.size + if (showLoadingFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < searchResult.size) {
            TYPE_ITEM
        } else {
            TYPE_LOADING_FOOTER
        }
    }

    fun showLoadingFooter(show: Boolean) {
        if (show == showLoadingFooter) return // no change

        showLoadingFooter = show
        if (show) {
            // Insert a new footer item at “position = dataList.size”
            notifyItemInserted(itemCount)
        } else {
            // Remove the footer item (at the old position)
            notifyItemRemoved(itemCount)
        }
    }

}