package com.gitsample.gitfinder.data.model

import com.google.gson.annotations.SerializedName

data class GitPublicRepositoriesModel(
    val name: String,
    val description: String?,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("stargazers_count")
    val startCount: Int
)