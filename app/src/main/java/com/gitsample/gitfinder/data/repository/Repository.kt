package com.gitsample.gitfinder.data.repository

import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.model.UserSearchData
import com.gitsample.gitfinder.data.remote.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

interface GitFinderRepository {

    suspend fun getUserProfile(userName: String): Response<UserModel>
    suspend fun getPublicRepositories(userName: String): Response<List<GitPublicRepositoriesModel>>
    suspend fun searchForUsers(
        searchQuery: String,
        pageNumber: Int,
        perPage: Int
    ): Response<UserSearchData>

}

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    GitFinderRepository {
    override suspend fun getUserProfile(userName: String): Response<UserModel> {
        return remoteDataSource.getUserProfile(userName)
    }

    override suspend fun getPublicRepositories(userName: String): Response<List<GitPublicRepositoriesModel>> {
        return remoteDataSource.getRepositories(userName)
    }

    override suspend fun searchForUsers(
        searchQuery: String,
        pageNumber: Int,
        perPage: Int
    ): Response<UserSearchData> {
        return remoteDataSource.searchForUsers(searchQuery, pageNumber, perPage)
    }

}