package com.rezalaki.booksexplorer.ui.archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rezalaki.booksexplorer.data.api.ApiHandlerState
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.data.repository.BooksRepository
import com.rezalaki.booksexplorer.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor (
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArchiveUiState>(ArchiveUiState.Loading)
    val uiState = _uiState.asStateFlow().asLiveData()

    suspend fun fetchBooks() = viewModelScope.launch(Dispatchers.IO) {
        booksRepository
            .fetchAllSavedBooksDb()
            .collect { fetch ->
                val uiState = when(fetch.state){
                    ApiHandlerState.SUCCESS -> {
                        val booksList = fetch.data as List<Book>
                        if (booksList.isEmpty()) {
                            ArchiveUiState.EmptyData
                        } else {
                            ArchiveUiState.FetchSuccess(booksList)
                        }
                    }
                    ApiHandlerState.FAILED -> ArchiveUiState.FetchFailed(fetch.errorMessage.toString())
                    ApiHandlerState.LOADING -> ArchiveUiState.Loading
                }
                _uiState.emit(uiState)
            }
    }

}