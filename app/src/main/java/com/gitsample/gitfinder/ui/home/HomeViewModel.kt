package com.gitsample.gitfinder.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.gitsample.gitfinder.data.model.UserItem
import com.gitsample.gitfinder.data.model.UserModel
import com.gitsample.gitfinder.data.model.UserSearchData
import com.gitsample.gitfinder.data.repository.GitFinderRepository
import com.gitsample.gitfinder.generics.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: GitFinderRepository) : ViewModel() {

    private var _searchResultData = Channel<PagingData<UserItem>>()
    val searchResultData = _searchResultData.receiveAsFlow()

    private var _currentUserProfile = MutableLiveData<ApiResult<UserModel>>()
    val currentUserProfile: LiveData<ApiResult<UserModel>> = _currentUserProfile


    fun searchForUsers(searchQuery: String, perPage: Int) {
        viewModelScope.launch {
            repository.searchForUsers(searchQuery, perPage).cachedIn(viewModelScope).collect {
                _searchResultData.send(it)
            }
        }
    }

    fun getUserProfile(login: String) {
        _currentUserProfile.value = ApiResult.Loading
        viewModelScope.launch {
            _currentUserProfile.value = repository.getUserProfile(login)
        }
    }


}