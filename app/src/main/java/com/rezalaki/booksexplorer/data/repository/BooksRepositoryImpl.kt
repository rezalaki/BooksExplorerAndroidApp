package com.rezalaki.booksexplorer.data.repository


import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.core.graphics.toColorInt
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.db.BookDao
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.util.Constants.API_RESPONSE_LIMIT_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val bookDao: BookDao
) : BooksRepository {

    override suspend fun searchBooksApi(title: String): Flow<ApiHandler<List<Book>>> {
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

        }.catch { throwable ->
            val errorMsg = throwable.message ?: "Default Error"
            emit(
                ApiHandler.error(errorMsg)
            )
        }
    }

    override suspend fun saveBookDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            bookDao.saveBook(book)
            emit(ApiHandler.success(true))

        }.catch {
            emit(ApiHandler.error("ERROR IN SAVING TO DB"))
        }
    }

    override suspend fun deleteBookDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            bookDao.deleteBook(book)
            emit(ApiHandler.success(true))

        }.catch {
            emit(ApiHandler.error("ERROR IN DELETING FROM DB"))
        }
    }

    override suspend fun isBookSavedDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            val isBookSaved = bookDao.isBookSaved(book.id) > 0

            emit(ApiHandler.success(isBookSaved))

        }.catch {
            emit(ApiHandler.error("ERROR IN SEARCHING IN DB"))
        }
    }

    override suspend fun fetchAllSavedBooksDb(): Flow<ApiHandler<List<Book>>> =
        flow {
            emit(ApiHandler.loading())

            val books = bookDao.getAllBooks()

            emit(ApiHandler.success(books))

        }.catch {
            emit(ApiHandler.error("ERROR IN FETCHING FROM DB, ${it.message.orEmpty()}"))
        }

    override suspend fun searchBooksPaginationApi(title: String): Flow<PagingData<Book>> =
        Pager(
            config = PagingConfig(
                pageSize = API_RESPONSE_LIMIT_COUNT,
                enablePlaceholders = true,
                initialLoadSize = API_RESPONSE_LIMIT_COUNT,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                BooksPagingSource(apiServices, title, API_RESPONSE_LIMIT_COUNT)
            }
        ).flow


}