package com.example.stockmarketappproject.presentation.mapper

import com.example.stockmarketappproject.data.model.CompanyListingData
import com.example.stockmarketappproject.presentation.model.CompanyListingPresentation

class CompanyListingPresentationMapperImpl : CompanyListingPresentationMapper {

    override fun CompanyListingData.toPresentation(): CompanyListingPresentation {
        return CompanyListingPresentation(name, symbol, exchange)
    }
}