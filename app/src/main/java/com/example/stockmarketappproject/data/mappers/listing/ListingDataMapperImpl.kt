package com.example.stockmarketappproject.data.mappers.listing

import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity
import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import javax.inject.Inject

class ListingDataMapperImpl @Inject constructor() : DefaultListingDataMapper {
    override fun CompanyListingEntity.toCompanyListingData(): CompanyListingData {
        return CompanyListingData(name, symbol, exchange)
    }

    override fun CompanyListingData.toCompanyListingEntity(): CompanyListingEntity {
        return CompanyListingEntity(name = name, symbol = symbol, exchange = exchange)
    }
}

