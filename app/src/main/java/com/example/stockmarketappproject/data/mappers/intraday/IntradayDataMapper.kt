package com.example.stockmarketappproject.data.mappers.intraday

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.data.remote.model.dto.DtoModel

interface IntradayDataMapper<in Dto : DtoModel, out Data : DataModel> {
    fun Dto.toCompanyIntradayInfoData(): Data
}