package com.example.stockmarketappproject.data.mappers

import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity
import com.example.stockmarketappproject.data.model.CompanyListingData

interface StockMapper {

    fun CompanyListingEntity.toCompanyListingData(): CompanyListingData
}