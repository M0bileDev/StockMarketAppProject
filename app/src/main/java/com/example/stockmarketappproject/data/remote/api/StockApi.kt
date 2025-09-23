package com.example.stockmarketappproject.data.remote.api

import com.example.stockmarketappproject.BuildConfig
import com.example.stockmarketappproject.data.remote.model.dto.info.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS&apikey=${BuildConfig.API_KEY}")
    suspend fun getListings(): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv&apikey=${BuildConfig.API_KEY}")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String
    ): ResponseBody

    @GET("query?function=OVERVIEW&apikey=${BuildConfig.API_KEY}")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String
    ): CompanyInfoDto
}