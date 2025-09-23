package com.example.stockmarketappproject.framework.di

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
}