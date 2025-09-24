package com.example.stockmarketappproject.presentation.model.listing

import com.example.stockmarketappproject.presentation.model.PresentationModel

data class CompanyListingPresentation(
    val name: String,
    val symbol: String,
    val exchange: String,
) : PresentationModel