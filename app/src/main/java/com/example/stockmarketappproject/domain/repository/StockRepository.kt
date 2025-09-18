package com.example.stockmarketappproject.domain.repository

import com.example.stockmarketappproject.domain.model.CompanyListingDomain
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    fun getCompanyListing(
        query: String
    ): Flow<Resource<List<CompanyListingDomain>>>

    suspend fun fetchCompanyListing(): Resource<List<CompanyListingDomain>>
}