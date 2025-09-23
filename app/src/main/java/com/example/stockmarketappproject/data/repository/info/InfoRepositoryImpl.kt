package com.example.stockmarketappproject.data.repository.info

import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

class InfoRepositoryImpl : DefaultInfoRepository {
    override fun getCompanyInfo(symbol: String): Flow<Resource<CompanyInfoData>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCompanyInfo(symbol: String): Flow<Resource<CompanyInfoData>> {
        TODO("Not yet implemented")
    }
}