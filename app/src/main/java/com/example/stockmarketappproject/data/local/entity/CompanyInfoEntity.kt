package com.example.stockmarketappproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_info_entity")
data class CompanyInfoEntity(
    val symbol: String,
    val description: String,
    @PrimaryKey
    val name: String,
    val country: String,
    val industry: String
) : EntityModel
