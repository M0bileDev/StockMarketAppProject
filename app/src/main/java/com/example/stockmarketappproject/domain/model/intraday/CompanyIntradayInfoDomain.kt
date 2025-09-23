package com.example.stockmarketappproject.domain.model.intraday

import com.example.stockmarketappproject.domain.model.DomainModel
import java.time.LocalDateTime

abstract class CompanyIntradayInfoDomain(
    open val timestamp: LocalDateTime,
    open val close: Double
) : DomainModel