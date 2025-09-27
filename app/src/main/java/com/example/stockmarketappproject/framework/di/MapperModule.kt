package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.data.mappers.info.DefaultInfoDataMapper
import com.example.stockmarketappproject.data.mappers.info.InfoDataMapperImpl
import com.example.stockmarketappproject.data.mappers.intraday.DefaultIntradayDataMapper
import com.example.stockmarketappproject.data.mappers.intraday.IntradayDataMapperImpl
import com.example.stockmarketappproject.data.mappers.listing.DefaultListingDataMapper
import com.example.stockmarketappproject.data.mappers.listing.ListingDataMapperImpl
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapperImpl
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapperImpl
import com.example.stockmarketappproject.presentation.mapper.listing.ListingPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.listing.ListingPresentationMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class MapperModule {

    @[Binds Singleton]
    abstract fun bindListingDataMapper(listingDataMapperImpl: ListingDataMapperImpl): DefaultListingDataMapper

    @[Binds Singleton]
    abstract fun bindListingPresentationMapper(listingPresentationMapperImpl: ListingPresentationMapperImpl): ListingPresentationMapper

    @[Binds Singleton]
    abstract fun bindInfoDataMapper(infoDataMapperImpl: InfoDataMapperImpl): DefaultInfoDataMapper

    @[Binds Singleton]
    abstract fun bindIntradayDataMapper(intradayDataMapperImpl: IntradayDataMapperImpl): DefaultIntradayDataMapper

    @[Binds Singleton]
    abstract fun bindInfoPresentationMapper(infoPresentationMapperImpl: InfoPresentationMapperImpl): InfoPresentationMapper

    @[Binds Singleton]
    abstract fun bindIntradayPresentationMapper(intradayPresentationMapperImpl: IntradayPresentationMapperImpl): IntradayPresentationMapper
}