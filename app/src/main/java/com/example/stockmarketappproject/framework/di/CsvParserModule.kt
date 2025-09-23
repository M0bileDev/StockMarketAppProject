package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.data.parser.listing.CsvListingParserImpl
import com.example.stockmarketappproject.data.parser.listing.DefaultCsvListingParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class CsvParserModule {

    @[Binds Singleton]
    abstract fun bindCsvListingParser(csvListingParser: CsvListingParserImpl): DefaultCsvListingParser
}