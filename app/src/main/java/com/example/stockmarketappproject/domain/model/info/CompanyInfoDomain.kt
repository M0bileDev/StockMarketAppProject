package com.example.stockmarketappproject.domain.model.info

import com.example.stockmarketappproject.domain.model.DomainModel

abstract class CompanyInfoDomain(
    open val symbol: String,
    open val description: String,
    open val name: String,
    open val country: String,
    open val industry: String
) : DomainModel