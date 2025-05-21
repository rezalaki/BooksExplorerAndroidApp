package com.rezalaki.booksexplorer.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.delay
import javax.inject.Inject

class BooksPagingSource @Inject constructor(
    private val apiServices: ApiServices,
    private val title: String,
    private val size: Int
) : PagingSource<Int, Book>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        delay(2_000)
        return try {
            val pageNumber = params.key ?: 1
            val response = apiServices.searchBooksPagination(title, pageNumber, size)
            val books = response.docs.map { it.toBook() }
            val nextPage = if (response.docs.isNotEmpty()) pageNumber + 1 else null
            val previousKey = if (pageNumber == 1) null else pageNumber - 1
            LoadResult.Page(
                data = books,
                nextKey = nextPage,
                prevKey = previousKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}