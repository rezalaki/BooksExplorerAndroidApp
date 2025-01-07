package com.rezalaki.booksexplorer.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rezalaki.booksexplorer.R
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.FragmentHomeBinding
import com.rezalaki.booksexplorer.ui.base.BaseFragment
import com.rezalaki.booksexplorer.util.gone
import com.rezalaki.booksexplorer.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val booksRvAdapter = BooksRvAdapter { clickedBook: Book ->
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(clickedBook)
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

        binding.rvMain.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = booksRvAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }
        })

        viewModel.uiState.observe(viewLifecycleOwner) { ui ->
            when (ui) {
                is HomeUiState.LoadFailed -> {
                    showErrorMessage(ui.errorMessage)
                    binding.boxError.apply {
                        tvError.text = resources.getString(R.string.failure)
                        root.visible()
                        btnRetry.visible()
                    }
                    binding.rvMain.gone()
                    binding.pbLoading.gone()
                }

                is HomeUiState.LoadSuccess -> {
                    binding.boxError.root.gone()
                    binding.pbLoading.gone()

                    if (booksRvAdapter.currentList != ui.data) {
                        binding.rvMain.scrollToPosition(0)
                        booksRvAdapter.apply {
                            submitList(null)
                            resetLastSeenPosition()
                        }
                    }

                    booksRvAdapter.submitList(ui.data)
                    binding.rvMain.visible()
                }

                HomeUiState.Loading -> {
                    binding.boxError.root.gone()
                    binding.rvMain.gone()
                    binding.pbLoading.visible()
                }

                HomeUiState.NoConnection -> {
                    showNetworkErrorMessage()
                    binding.boxError.apply {
                        tvError.text = resources.getString(R.string.no_internet)
                        btnRetry.gone()
                        root.visible()
                    }
                    binding.rvMain.gone()
                    binding.pbLoading.gone()
                }

                HomeUiState.EmptyData -> {
                    binding.boxError.apply {
                        tvError.text = resources.getString(R.string.no_data)
                        btnRetry.gone()
                    }
                    binding.pbLoading.gone()
                    binding.boxError.root.visible()
                }

                HomeUiState.EmptySearchInput -> {
                    binding.boxError.apply {
                        tvError.text = resources.getString(R.string.empty_input)
                        btnRetry.gone()
                        root.visible()
                    }
                    binding.pbLoading.gone()
                    binding.rvMain.gone()
                }
            }
        }

        binding.boxError.btnRetry.setOnClickListener {
            val query = binding.searchView.query.toString().trim()
            viewModel.search(query)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}