package com.rezalaki.booksexplorer.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rezalaki.booksexplorer.databinding.ItemLoadingBinding


class LoadingStatePagedAdapter(
    private val onRetryClicked: () -> Unit
) : LoadStateAdapter<LoadingStatePagedAdapter.LoadingAdapterViewHolder>() {

    inner class LoadingAdapterViewHolder(private val itemLoadingBinding: ItemLoadingBinding) :
        ViewHolder(itemLoadingBinding.root) {
        fun bind(loadState: LoadState) {
            itemLoadingBinding.progressCircular.isVisible = loadState is LoadState.Loading

            itemLoadingBinding.btnRetry.isVisible = loadState is LoadState.Error
            itemLoadingBinding.btnRetry.setOnClickListener {
                onRetryClicked.invoke()
            }

            itemLoadingBinding.tvError.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                itemLoadingBinding.tvError.text = loadState.error.localizedMessage
            }
        }
    }

    override fun onBindViewHolder(holder: LoadingAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingAdapterViewHolder {
        val itemView = ItemLoadingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingAdapterViewHolder(itemView)
    }

}