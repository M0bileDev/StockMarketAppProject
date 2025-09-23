package com.example.stockmarketappproject.data.model.intraday

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.domain.model.intraday.CompanyIntradayInfoDomain
import java.time.LocalDateTime

data class CompanyIntradayInfoData(
    override val timestamp: LocalDateTime,
    override val close: Double
) : DataModel, CompanyIntradayInfoDomain(
    timestamp,
    close
)