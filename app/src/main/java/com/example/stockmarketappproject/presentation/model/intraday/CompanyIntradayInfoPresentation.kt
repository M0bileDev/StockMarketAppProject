package com.example.stockmarketappproject.presentation.model.intraday

import com.example.stockmarketappproject.presentation.model.PresentationModel
import java.time.LocalDateTime

data class CompanyIntradayInfoPresentation(
    val timestamp: LocalDateTime,
    val close: Double
) : PresentationModel
