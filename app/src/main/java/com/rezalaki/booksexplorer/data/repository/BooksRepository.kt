package com.rezalaki.booksexplorer.data.repository

import androidx.paging.PagingData
import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.flow.Flow


interface BooksRepository {
    suspend fun searchBooksApi(title: String): Flow<ApiHandler<List<Book>>>

    suspend fun saveBookDb(book: Book): Flow<ApiHandler<Boolean>>
    suspend fun deleteBookDb(book: Book): Flow<ApiHandler<Boolean>>
    suspend fun isBookSavedDb(book: Book): Flow<ApiHandler<Boolean>>
    suspend fun fetchAllSavedBooksDb(): Flow<ApiHandler<List<Book>>>

    suspend fun searchBooksPaginationApi(title: String): Flow<PagingData<Book>>

}