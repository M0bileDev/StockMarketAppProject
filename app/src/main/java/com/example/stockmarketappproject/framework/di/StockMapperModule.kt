package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.data.mappers.DefaultStockDataMapper
import com.example.stockmarketappproject.data.mappers.StockDataMapperImpl
import com.example.stockmarketappproject.presentation.mapper.DefaultStockPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.StockPresentationMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class StockMapperModule {

    @[Binds Singleton]
    abstract fun bindStockDataMapper(stockDataMapperImpl: StockDataMapperImpl): DefaultStockDataMapper

    @[Binds Singleton]
    abstract fun bindStockPresentationMapper(stockPresentationMapperImpl: StockPresentationMapperImpl): DefaultStockPresentationMapper
}