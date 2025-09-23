package com.example.stockmarketappproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stockmarketappproject.data.local.entity.CompanyIntradayInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntradayDao {

    @Insert
    suspend fun insertCompanyIntradayInfo(
        companyIntradayInfoEntities: List<CompanyIntradayInfoEntity>
    )

    @Query("DELETE FROM company_intraday_info_entity WHERE name == :name")
    suspend fun deleteCompanyIntradayInfo(name: String)

    @Query(
        """
        SELECT * 
        FROM company_intraday_info_entity 
        WHERE LOWER(name) == :name
            """
    )
    fun getCompanyIntradayInfoEntities(name: String): Flow<List<CompanyIntradayInfoEntity>>
}