package com.rezalaki.booksexplorer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rezalaki.booksexplorer.R
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.FragmentHomeBinding
import com.rezalaki.booksexplorer.ui.base.BaseFragment
import com.rezalaki.booksexplorer.ui.home.adapters.BooksPagedAdapter
import com.rezalaki.booksexplorer.ui.home.adapters.BooksRvAdapter
import com.rezalaki.booksexplorer.ui.home.adapters.LoadingStatePagedAdapter
import com.rezalaki.booksexplorer.util.Constants.USE_PAGINATION
import com.rezalaki.booksexplorer.util.enterByScaleAnimation
import com.rezalaki.booksexplorer.util.gone
import com.rezalaki.booksexplorer.util.onTextChanged
import com.rezalaki.booksexplorer.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()


    private val booksRvAdapter = BooksRvAdapter { clickedBook: Book ->
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToDetailFragment(clickedBook)
        )
    }

    private val booksPaginationAdapter = BooksPagedAdapter { clickedBook ->
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToDetailFragment(clickedBook)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            binding.searchView
                .onTextChanged()
                .debounce(350L)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collectLatest { txt ->
                    if (USE_PAGINATION) {
                        viewModel.searchPagination(txt)
                    } else {
                        viewModel.search(txt)
                    }
                }
        }


        viewModel.uiState.observe(viewLifecycleOwner) { ui ->
            when (ui) {
                is HomeUiState.LoadFailed -> uiLoadBooksFailed(ui.errorMessage)

                is HomeUiState.LoadNonePaginatedSuccess -> uiLoadBooksSuccessed(ui.data)

                HomeUiState.Loading -> uiLoading()

                HomeUiState.NoConnection -> uiNoConnectionError()

                HomeUiState.EmptyData -> uiEmptyResult()

                HomeUiState.EmptySearchInput -> uiEmptySearchInput()

                is HomeUiState.LoadPagination -> uiLoadPaginatedData(ui.data)
            }
        }


        binding.rvMain.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            if (USE_PAGINATION) {
//                val loadingStateAdapter = LoadingStatePagedAdapter()
                adapter = booksPaginationAdapter

                booksPaginationAdapter.addLoadStateListener {
                    Log.d(
                        "TAGGGGGGG",
                        ">>=== isIdle:${it.isIdle} | hasError:${it.hasError} | refresh:${it.refresh} | mediator:${it.mediator} | append:${it.append} | prepend:${it.prepend} "
                    );
                }
                booksPaginationAdapter.addOnPagesUpdatedListener {
                    Log.d("TAGGGGGGG", "-->> PagesUpdatedListener");
                }
                booksPaginationAdapter.withLoadStateFooter(
                    footer = LoadingStatePagedAdapter {
                        booksPaginationAdapter.retry()
                    }
                )

            } else {
                adapter = booksRvAdapter
            }
        }

        binding.boxError.btnRetry.setOnClickListener {
            val query = binding.searchView.query.toString().trim()
            viewModel.search(query)
        }

        binding.fabSavedBooks.apply {
            enterByScaleAnimation()
            setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_archiveFragment)
            }
        }

    }

    private fun uiLoadPaginatedData(books: PagingData<Book>) {
        booksPaginationAdapter.submitData(lifecycle, books)
    }

    private fun uiEmptySearchInput() {
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.empty_input)
            btnRetry.gone()
            root.visible()
        }
        binding.pbLoading.gone()
        binding.rvMain.gone()
    }

    private fun uiEmptyResult() {
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.no_data)
            btnRetry.gone()
        }
        binding.pbLoading.gone()
        binding.boxError.root.visible()
    }

    private fun uiNoConnectionError() {
        showNetworkErrorMessage()
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.no_internet)
            btnRetry.gone()
            root.visible()
        }
        binding.rvMain.gone()
        binding.pbLoading.gone()
    }

    private fun uiLoading() {
        binding.boxError.root.gone()
        binding.rvMain.gone()
        binding.pbLoading.visible()
    }

    private fun uiLoadBooksSuccessed(booksList: List<Book>) {
        binding.boxError.root.gone()
        binding.pbLoading.gone()
        if (booksRvAdapter.currentList != booksList) {
            binding.rvMain.scrollToPosition(0)
            booksRvAdapter.apply {
                submitList(null)
                resetLastSeenPosition()
            }
        }

        booksRvAdapter.submitList(booksList)
        binding.rvMain.visible()
    }

    private fun uiLoadBooksFailed(errorMessage: String) {
        showErrorMessage(errorMessage)
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.failure)
            root.visible()
            btnRetry.visible()
        }
        binding.rvMain.gone()
        binding.pbLoading.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}