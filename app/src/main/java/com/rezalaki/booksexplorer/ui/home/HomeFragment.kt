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
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.LoadStates
import androidx.paging.PagedList
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rezalaki.booksexplorer.R
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.FragmentHomeBinding
import com.rezalaki.booksexplorer.databinding.ItemBookBinding
import com.rezalaki.booksexplorer.ui.base.BaseFragment
import com.rezalaki.booksexplorer.util.LoadingStateAdapter
import com.rezalaki.booksexplorer.util.enterByScaleAnimation
import com.rezalaki.booksexplorer.util.gone
import com.rezalaki.booksexplorer.util.loadImage
import com.rezalaki.booksexplorer.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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

        binding.pbLoading.gone()

        val booksAdapter = BookPagingAdapter()

        binding.rvMain.apply {
            adapter = booksAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
//        binding.rvMain.adapter = booksAdapter.withLoadStateFooter(
//            footer = LoadStateAdapter() {
//                booksAdapter.retry()
//            }
//        )
        booksAdapter.addLoadStateListener { load ->
            Log.d("TAGGGG", "addLoadStateListener state -> ${load.toString()}")
            if (load.source.refresh == LoadState.Loading) {
                binding.pbLoading.visible()
            } else {
                binding.pbLoading.gone()
            }
        }

        binding.rvMain.adapter = booksAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { booksAdapter.retry() }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.booksPaged.collectLatest {
                booksAdapter.submitData(it)
            }
        }


        /*
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
        */


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class BookPagingAdapter : PagingDataAdapter<Book, BookPagingAdapter.BookViewHolder2>(
    object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
            (oldItem.id == newItem.id)
    }
) {


    inner class BookViewHolder2(
        private val itemBinding: ItemBookBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(booka: Book?) {
            booka?.let { book: Book ->
                itemBinding.apply {
                    tvTitle.text = book.title
                    ivBanner.loadImage(book.imageUrl)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BookViewHolder2, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder2 {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder2(binding)
    }

}