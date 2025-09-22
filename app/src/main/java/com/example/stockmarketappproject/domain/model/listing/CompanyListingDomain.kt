package com.example.stockmarketappproject.domain.model.listing

import com.example.stockmarketappproject.domain.model.DomainModel

abstract class CompanyListingDomain(
    open val name: String,
    open val symbol: String,
    open val exchange: String,
) : DomainModel