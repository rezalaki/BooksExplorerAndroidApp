package com.rezalaki.booksexplorer.data.repository


import android.util.Log
import com.rezalaki.booksexplorer.data.api.ApiHandler
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.db.BookDao
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
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveBookDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            bookDao.saveBook(book)
            emit(ApiHandler.success(true))

        }.catch {
            emit(ApiHandler.error("ERROR IN SAVING TO DB"))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteBookDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            bookDao.deleteBook(book)
            emit(ApiHandler.success(true))

        }.catch {
            emit(ApiHandler.error("ERROR IN DELETING FROM DB"))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun isBookSavedDb(book: Book): Flow<ApiHandler<Boolean>> {
        return flow {

            emit(ApiHandler.loading())
            val books = bookDao.getAllBooks()
            Log.d("TAGGGGGGG", "all books in DB");
            Log.d("TAGGGGGGG", "$books");

            val isBookFound = books.any { it.id == book.id }
            emit(ApiHandler.success(isBookFound))

        }.catch {
            emit(ApiHandler.error("ERROR IN SEARCHING IN DB"))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun fetchAllSavedBooksDb(): Flow<ApiHandler<List<Book>>> =
        flow {
            emit(ApiHandler.loading())

            val books = bookDao.getAllBooks()

            emit( ApiHandler.success(books) )

        }.catch {
            emit( ApiHandler.error("ERROR IN FETCHING FROM DB, ${it.message.orEmpty()}") )

        }.flowOn(Dispatchers.IO)

}