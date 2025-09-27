package com.example.stockmarketappproject.presentation.mapper.listing

import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingPresentation
import javax.inject.Inject

class ListingPresentationMapperImpl @Inject constructor(): ListingPresentationMapper {

    override fun CompanyListingData.toPresentation(): CompanyListingPresentation {
        return CompanyListingPresentation(name, symbol, exchange)
    }
}