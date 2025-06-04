package com.gitsample.gitfinder.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("avatar_url")
    val avatar: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("login")
    val userId: String,
    val bio: String?,
    val followers: Int,
)

data class UserSearchData(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val inCompleteResults: Boolean,
    val items: List<UserItem>
)

data class UserItem(
    val login: String,
    @SerializedName("avatar_url")
    val avatar: String,
)

