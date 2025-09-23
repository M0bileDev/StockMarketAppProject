package com.example.stockmarketappproject.data.repository.intraday

import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow

class IntradayRepositoryImpl : DefaultIntradayRepository{
    
    override fun getIntradayInfo(symbol: String): Flow<Resource<List<CompanyIntradayInfoData>>> {
        TODO("Not yet implemented")
    }

    override fun fetchIntradayInfo(symbol: String): Flow<Resource<List<CompanyIntradayInfoData>>> {
        TODO("Not yet implemented")
    }

}