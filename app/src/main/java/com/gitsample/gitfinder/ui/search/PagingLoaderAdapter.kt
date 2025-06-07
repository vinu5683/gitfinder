package com.gitsample.gitfinder.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsample.gitfinder.R


class UserLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging_loader_item, parent, false)
        return LoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(
        itemView: View,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(loadState: LoadState) {
            progressBar.visibility = if (loadState is LoadState.Loading || loadState is LoadState.Error) View.VISIBLE else View.GONE
        }
    }
}