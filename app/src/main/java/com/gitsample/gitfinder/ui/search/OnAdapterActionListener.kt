package com.gitsample.gitfinder.ui.search

import com.gitsample.gitfinder.data.model.UserItem

interface OnAdapterActionListener {
    fun onClick(item: UserItem)
}