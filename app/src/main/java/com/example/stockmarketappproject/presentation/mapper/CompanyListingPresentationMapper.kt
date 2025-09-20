package com.example.stockmarketappproject.presentation.mapper

import com.example.stockmarketappproject.data.model.CompanyListingData
import com.example.stockmarketappproject.presentation.model.CompanyListingPresentation

interface CompanyListingPresentationMapper {

    fun CompanyListingData.toPresentation(): CompanyListingPresentation
}