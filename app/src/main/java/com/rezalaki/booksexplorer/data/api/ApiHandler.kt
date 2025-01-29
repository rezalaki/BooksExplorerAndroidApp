package com.rezalaki.booksexplorer.data.api

import android.util.Log

data class ApiHandler<out T>(
    val state: ApiHandlerState,
    val data: Any? = null,
    val errorMessage:String? = null
) {
    companion object {
        fun <T> loading(): ApiHandler<T> = ApiHandler(ApiHandlerState.LOADING)
        fun <T> error(errorMessage: String): ApiHandler<T> {
            Log.d("TAGGGGGGG", "ApiHandlerState.FAILED -> $errorMessage");
            return ApiHandler(ApiHandlerState.FAILED, errorMessage = errorMessage)
        }
        fun <T> success(result: T): ApiHandler<T> = ApiHandler(ApiHandlerState.SUCCESS, data = result)
    }
}

enum class ApiHandlerState {
    SUCCESS, FAILED, LOADING
}