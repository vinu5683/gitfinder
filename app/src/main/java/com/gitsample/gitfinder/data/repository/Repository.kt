package com.gitsample.gitfinder.data.repository

import androidx.paging.PagingData
import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.remote.RemoteDataSource
import com.gitsample.gitfinder.generics.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface GitFinderRepository {

    suspend fun getUserProfile(userName: String): ApiResult<UserModel>
    suspend fun getPublicRepositories(userName: String): Response<List<GitPublicRepositoriesModel>>
    suspend fun searchForUsers(
        searchQuery: String,
        perPage: Int
    ): Flow<PagingData<UserItem>>

}

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    GitFinderRepository {

    override suspend fun getUserProfile(userName: String): ApiResult<UserModel> {
        val data = remoteDataSource.getUserProfile(userName)
        return if (data.isSuccessful && data.code() == 200 && data.body() != null) {
            ApiResult.Success(data.body()!!)
        } else {
            ApiResult.Error(data.message())
        }
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