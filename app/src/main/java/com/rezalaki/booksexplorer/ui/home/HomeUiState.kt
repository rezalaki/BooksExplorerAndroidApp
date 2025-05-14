package com.rezalaki.booksexplorer.ui.home

import androidx.paging.PagingData
import com.rezalaki.booksexplorer.data.model.Book


sealed class HomeUiState {

    data object Loading: HomeUiState()

    data object NoConnection : HomeUiState()

    data object EmptyData : HomeUiState()

    data object EmptySearchInput : HomeUiState()

    data class LoadNonePaginatedSuccess(val data: List<Book>): HomeUiState()
    data class LoadPagination(val data: PagingData<Book>): HomeUiState()

    data class LoadFailed(val errorMessage: String) : HomeUiState()

}