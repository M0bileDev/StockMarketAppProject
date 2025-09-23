package com.example.stockmarketappproject.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface ListingApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(): ResponseBody
}