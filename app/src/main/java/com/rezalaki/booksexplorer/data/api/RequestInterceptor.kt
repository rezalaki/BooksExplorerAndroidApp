package com.rezalaki.booksexplorer.data.api


import com.rezalaki.booksexplorer.util.Constants
import okhttp3.Interceptor
import okhttp3.Response


class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url.newBuilder()
            .addQueryParameter("limit", Constants.API_RESPONSE_LIMIT_COUNT)
            .build()
        val newRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .url(httpUrl)
            .build()
        return chain.proceed(newRequest)
    }
}