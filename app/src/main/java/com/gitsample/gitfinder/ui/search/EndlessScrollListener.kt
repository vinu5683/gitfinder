package com.gitsample.gitfinder.ui.search

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val isLastPage: () -> Boolean,
    private val isLoading: () -> Boolean,
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val totalItemCount = layoutManager.itemCount
            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisiblePosition >= totalItemCount - 1
                && !isLoading()
                && !isLastPage()
            ) {
                onLoadMore()
            }
        }
    }
}