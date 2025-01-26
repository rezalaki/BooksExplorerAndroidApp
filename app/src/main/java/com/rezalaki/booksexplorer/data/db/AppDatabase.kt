package com.rezalaki.booksexplorer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rezalaki.booksexplorer.data.model.Book


@Database(
    entities = [Book::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getBookDao(): BookDao
}