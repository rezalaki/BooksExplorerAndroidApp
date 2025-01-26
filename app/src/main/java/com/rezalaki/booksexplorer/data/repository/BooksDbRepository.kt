package com.rezalaki.booksexplorer.data.repository

import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksDbRepository {
    suspend fun saveBookDb(book: Book): Flow<ApiHandler<Boolean>>
    suspend fun deleteBookDb(book: Book): Flow<ApiHandler<Boolean>>
    suspend fun isBookSavedDb(book: Book): Flow<ApiHandler<Boolean>>
}