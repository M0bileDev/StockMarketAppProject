package com.example.stockmarketappproject.data.local

import javax.inject.Inject

class ClearTableProviderImpl @Inject constructor(
    private val providers: Set<@JvmSuppressWildcards ClearTableProvider>
) : ClearTableProvider {
    override suspend fun clearTable() {
        providers.forEach { table ->
            table.clearTable()
        }
    }
}