package com.rezalaki.booksexplorer.data.repository

import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.data.model.BooksResponse
import kotlinx.coroutines.flow.Flow


interface BooksRepository {

    suspend fun searchBooks(title: String): Flow<ApiHandler<List<Book>>>

}