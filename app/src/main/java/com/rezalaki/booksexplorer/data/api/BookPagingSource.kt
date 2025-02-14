package com.rezalaki.booksexplorer.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.delay
import javax.inject.Singleton

@Singleton
class BookPagingSource(
    private val apiServices: ApiServices,
    private val title: String
) : PagingSource<Int, Book>() {

    companion object {
        const val PAGE_SIZE = 8
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {

            // to see loading better
            delay(3_000)

            val position = params.key ?: 1
            val response = apiServices.searchBooks(
                title,
                position,
                PAGE_SIZE
            )

            if (response.isSuccessful && response.body() != null) {
                val books = response.body()!!.docs.map { it.toBook() }
                LoadResult.Page(
                    data = books,
                    prevKey = if (position == 1) null else position.minus(1),
                    nextKey = if (position == response.body()!!.numFound / (PAGE_SIZE * position)) null
                    else position.plus(1)
                )
            } else {
                LoadResult.Error(Throwable("NO RESPONSE"))
            }

        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? = null


}