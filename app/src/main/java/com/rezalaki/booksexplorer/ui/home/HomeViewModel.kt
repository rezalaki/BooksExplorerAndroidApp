package com.rezalaki.booksexplorer.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rezalaki.booksexplorer.data.api.ApiHandlerState
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.data.repository.BooksRepository
import com.rezalaki.booksexplorer.util.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    companion object {
        const val DEBOUNCE_DELAY = 400L
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.EmptySearchInput)
    val uiState = _uiState.asStateFlow().asLiveData()


    private var jobSearch: Job? = null
    private var lastSearchedTitle = ""

    fun search(title: String) {
        if (lastSearchedTitle == title) {
            return
        }
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch(Dispatchers.IO) {
            if (networkChecker.isNetworkAvailable().not()) {
                _uiState.emit(HomeUiState.NoConnection)
                return@launch
            }
            if (title.length < 4) {
                _uiState.emit(HomeUiState.EmptySearchInput)
                return@launch
            }

            delay(DEBOUNCE_DELAY)
            booksRepository.searchBooks(title).collectLatest { api ->
                val uiState = when (api.state) {
                    ApiHandlerState.SUCCESS -> {
                        lastSearchedTitle = title

                        val booksList = api.data as List<Book>
                        if (booksList.isEmpty()) {
                            HomeUiState.EmptyData
                        } else {
                            HomeUiState.LoadSuccess(booksList)
                        }
                    }

                    ApiHandlerState.FAILED -> HomeUiState.LoadFailed(api.errorMessage!!)

                    ApiHandlerState.LOADING -> HomeUiState.Loading
                }
                _uiState.emit(uiState)
            }
        }
    }

}