package com.example.stockmarketappproject.data.model.listing

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.domain.model.listing.CompanyListingDomain

open class CompanyListingData(
    override val name: String,
    override val symbol: String,
    override val exchange: String,
) : DataModel, CompanyListingDomain(
    name,
    symbol,
    exchange
)