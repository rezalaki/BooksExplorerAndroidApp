package com.rezalaki.booksexplorer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.rezalaki.booksexplorer.data.model.Book


@Dao
interface BookDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM book")
    suspend fun getAllBooks(): List<Book>

}