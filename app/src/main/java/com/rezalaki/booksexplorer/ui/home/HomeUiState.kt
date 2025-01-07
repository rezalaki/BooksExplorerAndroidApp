package com.rezalaki.booksexplorer.ui.home

import com.rezalaki.booksexplorer.data.model.Book


sealed class HomeUiState {

    data object Loading: HomeUiState()

    data object NoConnection : HomeUiState()

    data object EmptyData : HomeUiState()

    data object EmptySearchInput : HomeUiState()

    data class LoadSuccess(val data: List<Book>): HomeUiState()

    data class LoadFailed(val errorMessage: String) : HomeUiState()

}