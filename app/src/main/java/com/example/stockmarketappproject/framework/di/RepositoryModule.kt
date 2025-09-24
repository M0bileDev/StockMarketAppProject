package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.info.InfoRepositoryImpl
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.data.repository.intraday.IntradayRepositoryImpl
import com.example.stockmarketappproject.data.repository.listing.DefaultListingRepository
import com.example.stockmarketappproject.data.repository.listing.ListingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class RepositoryModule {

    @[Binds Singleton]
    abstract fun bindListingRepository(listingRepositoryImpl: ListingRepositoryImpl): DefaultListingRepository

    @[Binds Singleton]
    abstract fun bindIntradayRepository(intradayRepositoryImpl: IntradayRepositoryImpl): DefaultIntradayRepository

    @[Binds Singleton]
    abstract fun bindInfoRepository(infoRepositoryImpl: InfoRepositoryImpl): DefaultInfoRepository
}