package com.example.stockmarketappproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_intraday_info_entity")
data class CompanyIntradayInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val timestamp: String,
    val close: Double
) : EntityModel
