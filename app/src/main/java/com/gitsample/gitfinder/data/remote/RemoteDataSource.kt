package com.gitsample.gitfinder.data.remote

import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.model.UserSearchData
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: GitFinderApiService) {

    suspend fun getUserProfile(userName: String): Response<UserModel> =
        apiService.getUserProfile(userName)

    suspend fun getRepositories(userName: String): Response<List<GitPublicRepositoriesModel>> =
        apiService.getRepositories(userName)

    suspend fun searchForUsers(searchQuery: String, pageNumber:Int, perPage:Int): Response<UserSearchData> =
        apiService.searchForUsers(searchQuery, pageNumber, perPage)

}