package com.rezalaki.booksexplorer.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rezalaki.booksexplorer.data.api.ApiServices
import com.rezalaki.booksexplorer.data.api.RequestInterceptor
import com.rezalaki.booksexplorer.util.Constants
import com.rezalaki.booksexplorer.util.NetworkChecker
import com.rezalaki.booksexplorer.util.ifIsDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .writeTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)

        httpClient.apply {
            addNetworkInterceptor(RequestInterceptor())

            ifIsDebug {
                val loggerInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addNetworkInterceptor(loggerInterceptor)
            }
        }

        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker =
        NetworkChecker(context.applicationContext)

}