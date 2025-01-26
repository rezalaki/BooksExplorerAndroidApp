package com.rezalaki.booksexplorer.data.repository

import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.flow.Flow


interface BooksApiRepository {
    suspend fun searchBooksApi(title: String): Flow<ApiHandler<List<Book>>>
}