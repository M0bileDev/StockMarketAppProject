package com.example.stockmarketappproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stockmarketappproject.data.local.entity.CompanyInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {

    @Insert
    suspend fun insertCompanyInfo(
        companyInfo: CompanyInfoEntity
    )

    @Query("DELETE FROM company_info_entity WHERE symbol == :query")
    suspend fun deleteCompanyInfo(query: String)

    @Query(
        """
        SELECT * 
        FROM company_info_entity 
        WHERE LOWER(symbol) == :query
        LIMIT 1
            """
    )
    fun getCompanyInfo(query: String): Flow<CompanyInfoEntity?>
}