package com.example.stockmarketappproject.data.mappers

interface StockDataMapper<Entity, Data> {

    fun Entity.toCompanyListingData(): Data
    fun Data.toCompanyListingEntity(): Entity
}