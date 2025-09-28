package com.example.stockmarketappproject.data.local

import javax.inject.Inject

class ClearTableProviderImpl @Inject constructor(
    private val tables: Set<@JvmSuppressWildcards ClearTableProvider>
) : ClearTableProvider {
    override suspend fun clearAllTables() {
        tables.forEach { table ->
            table.clearAllTables()
        }
    }
}