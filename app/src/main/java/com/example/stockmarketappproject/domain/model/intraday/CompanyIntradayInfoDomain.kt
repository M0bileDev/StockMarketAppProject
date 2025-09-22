package com.example.stockmarketappproject.domain.model.intraday

import com.example.stockmarketappproject.domain.model.DomainModel
import java.time.LocalDateTime

abstract class CompanyIntradayInfoDomain(
    val timestamp: LocalDateTime,
    val close: Double
): DomainModel