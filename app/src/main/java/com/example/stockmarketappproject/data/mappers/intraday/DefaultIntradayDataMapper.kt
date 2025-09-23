package com.example.stockmarketappproject.data.mappers.intraday

import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.remote.model.dto.intraday.CompanyIntradayInfoDto

interface DefaultIntradayDataMapper :
    IntradayDataMapper<CompanyIntradayInfoDto, CompanyIntradayInfoData>