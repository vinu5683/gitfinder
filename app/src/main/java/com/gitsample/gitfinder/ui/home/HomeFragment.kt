package com.gitsample.gitfinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gitsample.gitfinder.databinding.FragmentHomeBinding
import com.gitsample.gitfinder.generics.ApiResult
import com.gitsample.gitfinder.ui.search.EndlessScrollListener
import com.gitsample.gitfinder.ui.search.SearchProfileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchProfileAdapter
    private var isLoading = false
    private var isLastPage = false
    private val THRESHOLD = 3
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
        subscribeToSearchProfile()
        pagingRecyclerViewLogic()
    }

    private fun pagingRecyclerViewLogic() {
//        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(rv, dx, dy)
//
//
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                if (!isLoading && !isLastPage) {
//                    if (visibleItemCount + firstVisibleItemPosition + THRESHOLD >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                    ) {
//                        viewModel.loadNextPage(perPage = 30)
//                    }
//                }
//
//
//                viewModel.startNewSearch(searchQuery = "android", perPage = 30)
//            }
//        })
        val layoutManager = LinearLayoutManager(this.context)
        val scrollListener = EndlessScrollListener(
            layoutManager = layoutManager,
            isLastPage = { isLastPage },
            isLoading = { isLoading }
        ) {
            // This block only runs when RecyclerView is IDLE, at bottom, not loading, not last page.
            viewModel.loadNextPage(perPage = PER_PAGE_ITEMS_COUNT)
        }

        binding.rvSearchResult.addOnScrollListener(scrollListener)

    }

    private fun subscribeToSearchProfile() {
        viewModel.searchResultLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ApiResult.Loading -> {
                    if (adapter.itemCount == 0) {
                        showFullScreenLoading(true)
                    } else {
                        adapter.showLoadingFooter(true)
                    }
                    isLoading = true
                    binding.errorView.visibility = View.GONE
                }

                is ApiResult.Error -> {
                    showFullScreenLoading(false)
                    adapter.showLoadingFooter(false)
                    isLoading = false
                    binding.errorView.visibility = View.VISIBLE
                    binding.tvErrorText.text = it.errorMessage
                }

                is ApiResult.Success -> {
                    showFullScreenLoading(false)
                    adapter.showLoadingFooter(false)
                    binding.errorView.visibility = View.GONE
                    val combinedList = it.data
                    combinedList?.items?.let {
                        adapter.submitList(it)
                        if (it.isEmpty()) {
                            isLastPage = true
                        }
                    }
                    isLoading = false
                }
            }
        }
    }

    private fun showFullScreenLoading(show: Boolean) {
//        binding.progressOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setUpViews() {

        setUpRecyclerView()

        // 2) When user taps the mic icon:
        binding.textInputSearchBar.setEndIconOnClickListener {
            viewModel.startNewSearch(binding.etSearchBar.text.toString(), perPage = PER_PAGE_ITEMS_COUNT)
        }

        // 3) Optional: handle “Search” IME action
        binding.etSearchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text?.toString()?.trim() ?: ""
                if (query.length >= 3) {
                    viewModel.startNewSearch(query, perPage = PER_PAGE_ITEMS_COUNT)
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

    private fun setUpRecyclerView() {
        adapter = SearchProfileAdapter()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this.context)
        binding.rvSearchResult.adapter = adapter
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