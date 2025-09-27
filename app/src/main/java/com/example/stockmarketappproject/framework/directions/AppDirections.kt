package com.example.stockmarketappproject.framework.directions

import kotlinx.serialization.Serializable

@Serializable
data object CompanyListing

@Serializable
data class CompanyInfo(val symbol: String)