package com.example.stockmarketappproject.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface IntradayApi {

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String
    ): ResponseBody
}