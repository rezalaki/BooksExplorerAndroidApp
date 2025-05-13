package com.rezalaki.booksexplorer.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.model.BooksResponse
import javax.inject.Inject

class BooksPagingSource @Inject constructor(
    private val apiServices: ApiServices,
    private val title: String
) : PagingSource<Int, BooksResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BooksResponse> {
        try {

            val pageNumber = params.key ?: 1
            val response = apiServices.searchBooksPagination(title, pageNumber)
            if (response.) {

            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BooksResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}