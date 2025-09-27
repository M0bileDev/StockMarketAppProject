package com.example.stockmarketappproject.data.mappers.intraday

import com.example.stockmarketappproject.data.local.entity.EntityModel
import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.data.remote.model.dto.DtoModel

interface IntradayDataMapper<in Dto : DtoModel, Data : DataModel, Entity : EntityModel> {
    fun Dto.toCompanyIntradayInfoData(): Data
    fun Data.toCompanyIntradayInfoEntity(symbol: String): Entity
    fun Entity.toCompanyIntradayInfoData(): Data
}