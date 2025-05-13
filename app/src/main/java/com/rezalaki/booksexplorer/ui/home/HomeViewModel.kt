package com.rezalaki.booksexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rezalaki.booksexplorer.data.api.ApiHandlerState
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.data.repository.BooksRepository
import com.rezalaki.booksexplorer.util.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val networkChecker: NetworkChecker
) : ViewModel() {


    private val _uiState = MutableSharedFlow<HomeUiState>()
    val uiState = _uiState.asLiveData()


    fun search(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (networkChecker.isNetworkAvailable().not()) {
                _uiState.emit(HomeUiState.NoConnection)
                return@launch
            }

            booksRepository.searchBooksApi(title).collectLatest { api ->
                val uiState = when (api.state) {
                    ApiHandlerState.SUCCESS -> {

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