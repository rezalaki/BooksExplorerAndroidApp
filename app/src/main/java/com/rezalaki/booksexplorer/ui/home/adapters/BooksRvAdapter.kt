package com.rezalaki.booksexplorer.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.ItemBookBinding
import com.rezalaki.booksexplorer.util.enterByScaleAnimation
import com.rezalaki.booksexplorer.util.loadImage


class BooksRvAdapter(
    private val bookClicked: (clickedBook: Book) -> Unit
) : ListAdapter<Book, BooksRvAdapter.BookViewHolder>(
    object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean = oldItem.id == newItem.id
    }
) {

    private var lastSeenPosition = -1

    fun resetLastSeenPosition() {
        lastSeenPosition = -1
    }

    inner class BookViewHolder(
        private val itemBinding: ItemBookBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(book: Book) {
            if (lastSeenPosition < position) {
                lastSeenPosition = position
                itemBinding.root.enterByScaleAnimation()
            }
            itemBinding.apply {
                tvTitle.text = book.title
                ivBanner.loadImage(book.imageUrl)
                root.setOnClickListener {
                    bookClicked.invoke(book)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(getItem(position))
    }

}