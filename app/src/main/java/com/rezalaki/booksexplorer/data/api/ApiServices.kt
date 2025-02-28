package com.rezalaki.booksexplorer.data.api


import com.rezalaki.booksexplorer.data.model.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServices {

    @GET("search.json")
    suspend fun searchBooks(@Query("title") title: String? = null): Response<BooksResponse>

}