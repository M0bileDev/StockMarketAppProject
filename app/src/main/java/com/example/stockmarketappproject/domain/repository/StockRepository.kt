package com.example.stockmarketappproject.domain.repository

import com.example.stockmarketappproject.domain.model.DomainModel
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository<T : DomainModel> {

    fun getCompanyListing(
        query: String
    ): Flow<Resource<List<T>>>

    suspend fun fetchCompanyListing(): Flow<Resource<List<T>>>
}