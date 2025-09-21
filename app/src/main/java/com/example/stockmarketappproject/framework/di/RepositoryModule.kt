package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.data.repository.DefaultStockRepository
import com.example.stockmarketappproject.data.repository.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class RepositoryModule {

    @[Binds Singleton]
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): DefaultStockRepository
}