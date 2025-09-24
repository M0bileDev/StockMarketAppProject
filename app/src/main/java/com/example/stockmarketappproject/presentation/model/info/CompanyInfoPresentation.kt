package com.example.stockmarketappproject.presentation.model.info

import com.example.stockmarketappproject.presentation.model.PresentationModel

data class CompanyInfoPresentation(
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String
) : PresentationModel