package com.example.stockmarketappproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_listing_entity")
data class CompanyListingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val symbol: String,
    val exchange: String,
): EntityModel