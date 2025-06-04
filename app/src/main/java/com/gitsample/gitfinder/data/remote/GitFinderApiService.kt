package com.gitsample.gitfinder.data.remote

import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.model.UserSearchData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitFinderApiService {

    @GET("search/users")
    suspend fun searchForUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Response<UserSearchData>

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String
    ): Response<List<GitPublicRepositoriesModel>>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String
    ): Response<UserModel>

}