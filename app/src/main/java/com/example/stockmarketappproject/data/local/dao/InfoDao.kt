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

    @Query("DELETE FROM company_info_entity WHERE name == :name")
    suspend fun deleteCompanyInfo(name: String)

    @Query(
        """
        SELECT * 
        FROM company_info_entity 
        WHERE LOWER(name) == :name
        LIMIT 1
            """
    )
    fun getCompanyInfo(name: String): Flow<CompanyInfoEntity>
}