package com.example.stockmarketappproject.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stockmarketappproject.data.local.dao.InfoDao
import com.example.stockmarketappproject.data.local.dao.IntradayDao
import com.example.stockmarketappproject.data.local.dao.ListingDao
import com.example.stockmarketappproject.data.local.entity.CompanyInfoEntity
import com.example.stockmarketappproject.data.local.entity.CompanyIntradayInfoEntity
import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity

@Database(
    entities = [
        CompanyListingEntity::class,
        CompanyIntradayInfoEntity::class,
        CompanyInfoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract val listingDao: ListingDao
    abstract val intradayDao: IntradayDao
    abstract val infoDao: InfoDao
}