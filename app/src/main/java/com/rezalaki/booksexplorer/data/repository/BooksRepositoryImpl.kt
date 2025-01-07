package com.rezalaki.booksexplorer.data.repository


import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices
) : BooksRepository {

    override suspend fun searchBooks(title: String): Flow<ApiHandler<List<Book>>> {
        return flow {
            emit(ApiHandler.loading())

            val result = apiServices.searchBooks(title)
            if (result.isSuccessful && result.body() != null) {

                val mappedBooks = result.body()!!.docs.map { it.toBook() }

                emit(
                    ApiHandler.success(mappedBooks)
                )
            } else {
                emit(
                    ApiHandler.error(result.errorBody().toString())
                )
            }

        }.catch {throwable ->
            val errorMsg = throwable.message?: "Default Error"
            emit(
                ApiHandler.error(errorMsg)
            )
        }.flowOn(Dispatchers.IO)
    }

}