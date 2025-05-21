package com.rezalaki.booksexplorer.ui.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rezalaki.booksexplorer.databinding.ItemFooterBinding


class FooterPaginationStateAdapter(
    private val onRetryClicked: () -> Unit
) : LoadStateAdapter<FooterPaginationStateAdapter.LoadingAdapterViewHolder>() {

    inner class LoadingAdapterViewHolder(private val itemBinding: ItemFooterBinding) :
        ViewHolder(itemBinding.root) {
        fun bind(loadState: LoadState) {
            itemBinding.pbLoading.isVisible = loadState is LoadState.Loading

            itemBinding.btnRetry.isVisible = loadState is LoadState.Error
            itemBinding.tvError.isVisible = loadState is LoadState.Error

            itemBinding.btnRetry.setOnClickListener { onRetryClicked.invoke() }
        }
    }

    override fun onBindViewHolder(holder: LoadingAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingAdapterViewHolder {
        val itemView = ItemFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingAdapterViewHolder(itemView)
    }

}