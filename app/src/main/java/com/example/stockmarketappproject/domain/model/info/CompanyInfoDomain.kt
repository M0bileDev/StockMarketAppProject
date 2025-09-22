package com.example.stockmarketappproject.domain.model.info

import com.example.stockmarketappproject.domain.model.DomainModel

abstract class CompanyInfoDomain(
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String
) : DomainModel