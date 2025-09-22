package com.example.stockmarketappproject.data.remote.model.dto.intraday

import com.example.stockmarketappproject.data.remote.model.dto.DtoModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyIntradayInfoDto(
    @param:Json(name = "timestamp")
    val timestamp: String,
    @param:Json(name = "close")
    val close: Double
) : DtoModel