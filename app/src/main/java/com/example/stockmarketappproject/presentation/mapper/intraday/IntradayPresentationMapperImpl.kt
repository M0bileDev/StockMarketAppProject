package com.example.stockmarketappproject.presentation.mapper.intraday

import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.presentation.model.intraday.CompanyIntradayInfoPresentation
import javax.inject.Inject

class IntradayPresentationMapperImpl @Inject constructor(): IntradayPresentationMapper {
    override fun CompanyIntradayInfoData.toPresentation(): CompanyIntradayInfoPresentation {
        return CompanyIntradayInfoPresentation(
            timestamp,
            close
        )
    }
}