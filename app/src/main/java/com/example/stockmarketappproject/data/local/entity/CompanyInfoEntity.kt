package com.example.stockmarketappproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_info_entity")
data class CompanyInfoEntity(
    @PrimaryKey
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String
) : EntityModel
