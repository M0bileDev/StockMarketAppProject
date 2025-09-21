package com.example.stockmarketappproject.data.mappers

import com.example.stockmarketappproject.data.local.entity.EntityModel
import com.example.stockmarketappproject.data.model.DataModel

interface StockDataMapper<Entity : EntityModel, Data : DataModel> {

    fun Entity.toCompanyListingData(): Data
    fun Data.toCompanyListingEntity(): Entity
}