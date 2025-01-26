package com.rezalaki.booksexplorer.di

import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.db.AppDatabase
import com.rezalaki.booksexplorer.data.db.BookDao
import com.rezalaki.booksexplorer.data.repository.BooksApiRepository
import com.rezalaki.booksexplorer.data.repository.BooksRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBooksRepository(apiServices: ApiServices, bookDao: BookDao): BooksApiRepository =
        BooksRepositoryImpl(apiServices, bookDao)

}