package com.gitsample.gitfinder.data.repository

import androidx.paging.PagingData
import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface GitFinderRepository {

    suspend fun getUserProfile(userName: String): Response<UserModel>
    suspend fun getPublicRepositories(userName: String): Response<List<GitPublicRepositoriesModel>>
    suspend fun searchForUsers(
        searchQuery: String,
        perPage: Int
    ): Flow<PagingData<UserItem>>

}

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : GitFinderRepository {

    override suspend fun getUserProfile(userName: String): Response<UserModel> {
        return remoteDataSource.getUserProfile(userName)
    }

    override suspend fun getPublicRepositories(userName: String): Response<List<GitPublicRepositoriesModel>> {
        return remoteDataSource.getRepositories(userName)
    }

    override suspend fun searchForUsers(
        searchQuery: String,
        perPage: Int
    ): Flow<PagingData<UserItem>> {

        return remoteDataSource.searchForUsers(searchQuery, perPage)
    }

}