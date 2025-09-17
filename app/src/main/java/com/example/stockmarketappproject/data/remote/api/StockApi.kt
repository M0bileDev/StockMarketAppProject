package com.example.stockmarketappproject.data.remote.api

import com.example.stockmarketappproject.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET

interface StockApi {

    @GET("query?function=LISTING_STATUS&apikey=${BuildConfig.API_KEY}")
    suspend fun getListings() : ResponseBody
}