package com.example.stockmarketappproject.presentation.mapper

import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingPresentation
import javax.inject.Inject

class StockPresentationMapperImpl @Inject constructor(): DefaultStockPresentationMapper {

    override fun CompanyListingData.toPresentation(): CompanyListingPresentation {
        return CompanyListingPresentation(name, symbol, exchange)
    }
}