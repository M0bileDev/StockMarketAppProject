package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.BuildConfig
import com.example.stockmarketappproject.data.remote.api.StockApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@[Module InstallIn(SingletonComponent::class)]
object NetworkModule {

    @[Provides Singleton]
    fun provideStockApi(
        moshiConverterFactory: MoshiConverterFactory,
        httpClient: OkHttpClient
    ): StockApi {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create()
    }

    @[Provides Singleton]
    fun provideMoshiConverter(
        moshi: Moshi
    ): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @[Provides Singleton]
    fun provideMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @[Provides Singleton]
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @[Provides Singleton]
    fun provideHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}