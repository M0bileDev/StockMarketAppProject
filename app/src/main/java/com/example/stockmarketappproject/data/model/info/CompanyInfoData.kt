package com.example.stockmarketappproject.data.model.info

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.domain.model.info.CompanyInfoDomain

data class CompanyInfoData(
    override val symbol: String,
    override val description: String,
    override val name: String,
    override val country: String,
    override val industry: String
) : DataModel, CompanyInfoDomain(
    symbol,
    description,
    name,
    country,
    industry
)