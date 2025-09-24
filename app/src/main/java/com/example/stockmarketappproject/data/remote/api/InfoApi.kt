package com.example.stockmarketappproject.data.remote.api

import com.example.stockmarketappproject.data.remote.model.dto.info.CompanyInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface InfoApi {

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String
    ): CompanyInfoDto?
}