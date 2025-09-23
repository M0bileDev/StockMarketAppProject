package com.example.stockmarketappproject.domain.repository

import com.example.stockmarketappproject.domain.model.DomainModel
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

interface IntradayRepository<Domain : DomainModel> {

    fun getIntradayInfo(
        name: String
    ): Flow<Resource<List<Domain>>>

    fun fetchIntradayInfo(
        name: String
    ): Flow<Resource<List<Domain>>>
}