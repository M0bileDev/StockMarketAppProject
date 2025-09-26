package com.example.stockmarketappproject.domain.repository

import com.example.stockmarketappproject.domain.model.DomainModel
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

interface InfoRepository<Domain : DomainModel> {

    fun getCompanyInfo(
        name: String
    ): Flow<Resource<Domain?>>

    suspend fun fetchCompanyInfo(
        name: String
    ): Resource<Domain>
}