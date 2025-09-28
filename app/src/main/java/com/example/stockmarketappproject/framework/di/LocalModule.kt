package com.example.stockmarketappproject.framework.di

import android.content.Context
import androidx.room.Room
import com.example.stockmarketappproject.data.local.provider.ClearTableProvider
import com.example.stockmarketappproject.data.local.provider.ClearTableProviderImpl
import com.example.stockmarketappproject.data.local.dao.InfoDao
import com.example.stockmarketappproject.data.local.dao.IntradayDao
import com.example.stockmarketappproject.data.local.dao.ListingDao
import com.example.stockmarketappproject.data.local.database.StockDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object LocalModuleProvider {

    @[Provides Singleton]
    fun provideStockDatabase(
        @ApplicationContext context: Context
    ): StockDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = StockDatabase::class.java,
            name = "stock-database"
        ).build()
    }

    @[Provides Singleton]
    fun provideListingDao(
        stockDatabase: StockDatabase
    ): ListingDao {
        return stockDatabase.listingDao
    }

    @[Provides Singleton]
    fun provideIntradayDao(
        stockDatabase: StockDatabase
    ): IntradayDao {
        return stockDatabase.intradayDao
    }

    @[Provides Singleton]
    fun provideInfoDao(
        stockDatabase: StockDatabase
    ): InfoDao {
        return stockDatabase.infoDao
    }

    @[Provides ElementsIntoSet]
    fun provideClearTables(
        listingDao: ListingDao,
        intradayDao: IntradayDao,
        infoDao: InfoDao
    ): Set<ClearTableProvider> {
        return setOf(infoDao, listingDao, intradayDao)
    }
}

@[Module InstallIn(SingletonComponent::class)]
abstract class LocalModuleBinder {

    @[Binds Singleton]
    abstract fun bindClearTableProvider(clearTableImpl: ClearTableProviderImpl): ClearTableProvider
}