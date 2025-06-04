package com.gitsample.gitfinder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserSearchData
import com.gitsample.gitfinder.data.repository.GitFinderRepository
import com.gitsample.gitfinder.generics.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: GitFinderRepository) : ViewModel() {

    private var currentPage: Int = 1
    private var totalCount: Int = 0
    private var isLoading: Boolean = false
    private val currentItems = mutableListOf<UserItem>()
    private var lastQuery: String = ""

    private val _searchResultLiveData = MutableLiveData<ApiResult<UserSearchData?>>()
    val searchResultLiveData: LiveData<ApiResult<UserSearchData?>> = _searchResultLiveData


    private fun searchForUsers(searchQuery: String, pageNumber: Int, perPage: Int) {
        isLoading = true
        _searchResultLiveData.value = ApiResult.Loading
        viewModelScope.launch {
            try {
                val data = repository.searchForUsers(searchQuery, pageNumber, perPage)
                Log.d("TAGTAGTAG", "viewmodel successful: ${data.isSuccessful} ${data.code()}")
                if (data.isSuccessful && data.code() == 200) {
                    Log.d("TAGTAGTAG", "viewmodel code: ${data.isSuccessful}")

                    data.body()?.let {
                        Log.d("TAGTAGTAG", "viewmodel data: ${it}")
                        totalCount = it.totalCount
                        currentItems.addAll(it.items)
                        _searchResultLiveData.value = ApiResult.Success(it)
                    } ?: {
                        _searchResultLiveData.value = ApiResult.Error("Success With Error", null)
                    }
                } else {
                    _searchResultLiveData.value =
                        ApiResult.Error("Success With Error", throwable = Throwable(data.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }


    fun startNewSearch(searchQuery: String, perPage: Int) {
        lastQuery = searchQuery
        currentPage = 1
        totalCount = 0
        currentItems.clear()
        searchForUsers(searchQuery, currentPage, perPage)
    }

    fun loadNextPage(perPage: Int) {
        if (isLoading) return
        currentPage += 1
        searchForUsers(lastQuery, currentPage, perPage)
    }


}