package com.example.stockmarketappproject.framework.di

import android.content.Context
import androidx.room.Room
import com.example.stockmarketappproject.data.local.dao.StockDao
import com.example.stockmarketappproject.data.local.database.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object LocalModule {

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
    fun provideStockDao(
        stockDatabase: StockDatabase
    ): StockDao {
        return stockDatabase.stockDao
    }
}