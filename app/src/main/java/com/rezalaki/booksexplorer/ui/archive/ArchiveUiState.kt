package com.rezalaki.booksexplorer.ui.archive

import com.rezalaki.booksexplorer.data.model.Book


sealed class ArchiveUiState {
    data object Loading: ArchiveUiState()
    data object EmptyData : ArchiveUiState()
    data class FetchSuccess(val data: List<Book>): ArchiveUiState()
    data class FetchFailed(val errorMessage: String) : ArchiveUiState()
}