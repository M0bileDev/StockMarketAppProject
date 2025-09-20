package com.example.stockmarketappproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stockmarketappproject.data.local.dao.StockDao
import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity

@Database(
    entities = [CompanyListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract val stockDao: StockDao
}