package com.example.stockmarketappproject.domain.repository

import com.example.stockmarketappproject.domain.model.DomainModel
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

interface IntradayRepository<Domain : DomainModel> {

    fun getIntradayInfo(
        query: String
    ): Flow<Resource<List<Domain>>>

    suspend fun fetchIntradayInfo(
        symbol: String
    ): Resource<List<Domain>>
}