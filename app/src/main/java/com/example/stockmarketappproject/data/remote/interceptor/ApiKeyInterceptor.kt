package com.example.stockmarketappproject.data.remote.interceptor

import com.example.stockmarketappproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        //provide api key
        val newUrl = originalUrl
            .newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest
            .newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}