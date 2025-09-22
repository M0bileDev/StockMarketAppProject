package com.example.stockmarketappproject.data.remote.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyInfoDto(
    @param:Json(name = "Symbol")
    val symbol: String,
    @param:Json(name = "Description")
    val description: String,
    @param:Json(name = "Name")
    val name: String,
    @param:Json(name = "Country")
    val country: String,
    @param:Json(name = "Industry")
    val industry: String
)