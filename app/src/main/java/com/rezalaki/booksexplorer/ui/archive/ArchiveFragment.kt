package com.rezalaki.booksexplorer.ui.archive

import android.os.Bundle
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
import com.rezalaki.booksexplorer.databinding.FragmentArchiveBinding
import com.rezalaki.booksexplorer.databinding.FragmentHomeBinding
import com.rezalaki.booksexplorer.ui.base.BaseFragment
import com.rezalaki.booksexplorer.ui.home.BooksRvAdapter
import com.rezalaki.booksexplorer.ui.home.HomeFragmentDirections
import com.rezalaki.booksexplorer.ui.home.HomeUiState
import com.rezalaki.booksexplorer.ui.home.HomeViewModel
import com.rezalaki.booksexplorer.util.gone
import com.rezalaki.booksexplorer.util.visible
import kotlinx.coroutines.launch


class ArchiveFragment : BaseFragment() {
    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArchiveViewModel by viewModels()

    private val booksRvAdapter = BooksRvAdapter { clickedBook: Book ->
        findNavController().navigate(
            ArchiveFragmentDirections.actionArchiveFragmentToDetailFragment(clickedBook)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchBooks()

        binding.rvMain.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = booksRvAdapter
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.uiState.observe(viewLifecycleOwner) { ui ->
            when (ui) {
                is ArchiveUiState.FetchFailed -> uiFetchingFailed(ui.errorMessage)

                is ArchiveUiState.FetchSuccess -> uiFetchingSuccessed(ui.data)

                ArchiveUiState.Loading -> uiLoading()

                ArchiveUiState.EmptyData -> uiEmptyData()
            }
        }

        binding.boxError.btnRetry.setOnClickListener {
            fetchBooks()
        }

    }

    private fun uiEmptyData() {
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.no_data)
            btnRetry.gone()
        }
        binding.pbLoading.gone()
        binding.boxError.root.visible()
        binding.rvMain.gone()
    }

    private fun uiLoading() {
        binding.boxError.root.gone()
        binding.rvMain.gone()
        binding.pbLoading.visible()
    }

    private fun uiFetchingSuccessed(booksList: List<Book>) {
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

    private fun uiFetchingFailed(errorMessage: String) {
        showErrorMessage(errorMessage)
        binding.boxError.apply {
            tvError.text = resources.getString(R.string.failure)
            root.visible()
            btnRetry.visible()
        }
        binding.rvMain.gone()
        binding.pbLoading.gone()
    }

    private fun fetchBooks(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchBooks()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}