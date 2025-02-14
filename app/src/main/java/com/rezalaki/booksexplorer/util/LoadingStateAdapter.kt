package com.rezalaki.booksexplorer.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rezalaki.booksexplorer.databinding.ItemRvRetryBinding

class LoadingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        private val binding: ItemRvRetryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) {
            if (state is LoadState.Loading) {
                binding.retryButton.gone()
                binding.progressBar.visible()
            }
            if (state is LoadState.Error) {
                binding.retryButton.visible()
                binding.progressBar.gone()
            }
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemRvRetryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

}