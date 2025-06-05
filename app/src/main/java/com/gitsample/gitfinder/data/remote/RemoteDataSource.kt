package com.gitsample.gitfinder.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gitsample.gitfinder.data.model.GitPublicRepositoriesModel
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.ui.search.paging.GitUserPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: GitFinderApiService,
                                           private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var gitUserPagingSource:GitUserPagingSource? = null

    suspend fun getUserProfile(userName: String): Response<UserModel> =
        apiService.getUserProfile(userName)

    suspend fun getRepositories(userName: String): Response<List<GitPublicRepositoriesModel>> =
        apiService.getRepositories(userName)

    fun searchForUsers(searchQuery: String, perPage: Int)
            : Flow<PagingData<UserItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = perPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                gitUserPagingSource = GitUserPagingSource(apiService, searchQuery, perPage)
                gitUserPagingSource!!
            }
        ).flow.flowOn(ioDispatcher)

    }

    fun refreshSearchResults() {
        gitUserPagingSource?.invalidate()
    }

}