package com.gitsample.gitfinder.ui.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserSearchData
import com.gitsample.gitfinder.data.remote.GitFinderApiService
import retrofit2.HttpException
import java.io.IOException

class GitUserPagingSource(
    private val api: GitFinderApiService,
    private val query: String,
    private val perPage: Int
) : PagingSource<Int, UserItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = api.searchForUsers(query, pageNumber, perPage)
            if (response.isSuccessful) {
                val body: UserSearchData? = response.body()
                val items = body?.items.orEmpty()

                val nextKey = if (items.isEmpty()) {
                    null
                } else {
                    pageNumber + 1
                }

                LoadResult.Page(
                    data = items,
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (ioEx: IOException) {
            LoadResult.Error(ioEx)
        } catch (httpEx: HttpException) {
            LoadResult.Error(httpEx)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserItem>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}