package com.rezalaki.booksexplorer.ui.detail

sealed class DetailUiState {
    data object SaveToDbSuccess : DetailUiState()
    class SaveToDbFailed(val errorMessage: String) : DetailUiState()
    class BookFoundSuccess(val isBookFound: Boolean) : DetailUiState()
    class BookFoundFailed(val errorMessage: String) : DetailUiState()
    data object DeleteFromDbSuccess : DetailUiState()
    class DeleteFromDbFailed(val errorMessage: String) : DetailUiState()
    data object Loading : DetailUiState()
}
