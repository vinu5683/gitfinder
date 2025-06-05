package com.gitsample.gitfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitsample.gitfinder.databinding.FragmentHomeBinding
import com.gitsample.gitfinder.ui.search.SearchProfileAdapter
import com.gitsample.gitfinder.ui.search.UserLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchProfileAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        pagingRecyclerViewLogic()
        subscribeToSearchProfile()
        callSearchUsers()
    }

    private fun pagingRecyclerViewLogic() {
        adapter = SearchProfileAdapter()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResult.adapter = adapter.withLoadStateFooter(
            footer = UserLoadStateAdapter { adapter.retry() }
        )
        attachLoadingListener(adapter)
    }

    private fun subscribeToSearchProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResultData.collectLatest {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun setUpViews() {
        binding.etSearchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text?.toString()?.trim() ?: ""
                if (query.length >= 3) {
                    viewModel.searchForUsers(query, perPage = PER_PAGE_ITEMS_COUNT)
                } else {
                    Toast.makeText(
                        this.context,
                        "Type at least 3 characters to search",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            } else {
                false
            }
        }
    }

    private fun attachLoadingListener(searchProfileAdapter: SearchProfileAdapter) {
        searchProfileAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressOverlay.visibility = View.VISIBLE
            } else {
                binding.progressOverlay.visibility = View.GONE
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    errorState.error.printStackTrace()
                }

            }
        }
    }

    private fun callSearchUsers() {
        viewModel.searchForUsers("android", 10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TYPE_LOADING_FOOTER = 1
        const val TYPE_ITEM = 2
        const val PER_PAGE_ITEMS_COUNT = 10
    }
}