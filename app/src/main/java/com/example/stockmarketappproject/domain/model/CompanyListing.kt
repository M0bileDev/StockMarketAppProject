package com.example.stockmarketappproject.domain.model

abstract class CompanyListing(
    open val name: String,
    open val symbol: String,
    open val exchange: String,
)