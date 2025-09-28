package com.example.stockmarketappproject.data.local

interface ClearTableProvider {
    suspend fun clearAllTables()
}