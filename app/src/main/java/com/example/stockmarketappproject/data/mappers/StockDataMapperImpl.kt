package com.example.stockmarketappproject.data.mappers

import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity
import com.example.stockmarketappproject.data.model.CompanyListingData
import javax.inject.Inject

class StockDataMapperImpl @Inject constructor() : DefaultStockDataMapper {
    override fun CompanyListingEntity.toCompanyListingData(): CompanyListingData {
        return CompanyListingData(name, symbol, exchange)
    }

    override fun CompanyListingData.toCompanyListingEntity(): CompanyListingEntity {
        return CompanyListingEntity(name = name, symbol = symbol, exchange = exchange)
    }
}

