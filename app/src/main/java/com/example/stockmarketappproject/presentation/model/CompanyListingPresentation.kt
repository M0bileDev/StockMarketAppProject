package com.example.stockmarketappproject.presentation.model

data class CompanyListingPresentation(
    val name: String,
    val symbol: String,
    val exchange: String,
) : PresentationModel