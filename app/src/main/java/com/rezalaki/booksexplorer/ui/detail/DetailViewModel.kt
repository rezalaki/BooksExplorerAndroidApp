package com.rezalaki.booksexplorer.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rezalaki.booksexplorer.data.api.ApiHandlerState
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableSharedFlow<DetailUiState>()
    val uiState = _uiState.asSharedFlow().asLiveData()

    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksRepository.saveBookDb(book)
            .collectLatest {
                when (it.state) {
                    ApiHandlerState.SUCCESS -> {
                        _uiState.emit(DetailUiState.SaveToDbSuccess)
                    }

                    ApiHandlerState.FAILED -> {
                        _uiState.emit(DetailUiState.SaveToDbFailed(it.errorMessage!!))
                    }

                    ApiHandlerState.LOADING -> {
                        _uiState.emit(DetailUiState.Loading)
                    }
                }
            }
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksRepository.deleteBookDb(book)
            .collectLatest {
                when (it.state) {
                    ApiHandlerState.SUCCESS -> {
                        _uiState.emit(DetailUiState.DeleteFromDbSuccess)
                    }

                    ApiHandlerState.FAILED -> {
                        _uiState.emit(DetailUiState.DeleteFromDbFailed(it.errorMessage!!))
                    }

                    ApiHandlerState.LOADING -> {
                        _uiState.emit(DetailUiState.Loading)
                    }
                }
            }
    }

    fun searchBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksRepository.isBookSavedDb(book)
            .collectLatest {
                when (it.state) {
                    ApiHandlerState.SUCCESS -> {
                        val isFound = it.data as Boolean
                        _uiState.emit(DetailUiState.BookFoundSuccess(isFound))
                    }

                    ApiHandlerState.FAILED -> {
                        _uiState.emit(DetailUiState.BookFoundFailed(it.errorMessage!!))
                    }

                    ApiHandlerState.LOADING -> {
                        _uiState.emit(DetailUiState.Loading)
                    }
                }
            }
    }

}