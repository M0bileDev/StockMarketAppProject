package com.example.stockmarketappproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stockmarketappproject.data.local.ClearTableProvider
import com.example.stockmarketappproject.data.local.entity.CompanyIntradayInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntradayDao : ClearTableProvider  {

    @Insert
    suspend fun insertCompanyIntradayInfo(
        companyIntradayInfoEntities: List<CompanyIntradayInfoEntity>
    )

    @Query("DELETE FROM company_intraday_info_entity WHERE symbol == :query")
    suspend fun deleteCompanyIntradayInfo(query: String)

    @Query(
        """
        SELECT * 
        FROM company_intraday_info_entity 
        WHERE symbol == :query
            """
    )
    fun getCompanyIntradayInfoEntities(query: String): Flow<List<CompanyIntradayInfoEntity>>

    @Query("DELETE FROM company_intraday_info_entity")
    suspend fun clearIntraday()

    override suspend fun clearAllTables() = clearIntraday()
}