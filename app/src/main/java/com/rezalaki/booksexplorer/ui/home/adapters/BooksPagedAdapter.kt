package com.rezalaki.booksexplorer.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.ItemBookBinding
import com.rezalaki.booksexplorer.util.loadImage

class BooksPagedAdapter(
    private val onBookClicked: (Book) -> Unit
) : PagingDataAdapter<Book, BooksPagedAdapter.PagedBookViewHolder>(
    object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {


    inner class PagedBookViewHolder(
        private val itemBinding: ItemBookBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(book: Book){
            itemBinding.apply {
                tvTitle.text = book.title
                ivBanner.loadImage(book.imageUrl)
                root.setOnClickListener {
                    onBookClicked.invoke(book)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PagedBookViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (getItem(position) != null)
            holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedBookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagedBookViewHolder(binding)
    }

}