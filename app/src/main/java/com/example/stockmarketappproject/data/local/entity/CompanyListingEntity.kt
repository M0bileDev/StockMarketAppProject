package com.example.stockmarketappproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stockmarketappproject.framework.CompanyListing

@Entity(tableName = "company_listing_entity")
data class CompanyListingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    override val name: String,
    override val symbol: String,
    override val exchange: String,
) : CompanyListing(
    name,
    symbol,
    exchange
)