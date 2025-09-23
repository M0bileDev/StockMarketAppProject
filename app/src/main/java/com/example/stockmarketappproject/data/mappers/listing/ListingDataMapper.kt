package com.example.stockmarketappproject.data.mappers.listing

import com.example.stockmarketappproject.data.local.entity.EntityModel
import com.example.stockmarketappproject.data.model.DataModel

interface ListingDataMapper<Entity : EntityModel, Data : DataModel> {

    fun Entity.toCompanyListingData(): Data
    fun Data.toCompanyListingEntity(): Entity
}