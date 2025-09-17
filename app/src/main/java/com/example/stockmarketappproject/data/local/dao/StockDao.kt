package com.example.stockmarketappproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stockmarketappproject.data.local.entity.CompanyListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Insert
    suspend fun insertCompanyListing(
        companyListingEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM company_listing_entity")
    suspend fun clearCompanyListings()

    @Query(
        """
        SELECT * 
        FROM company_listing_entity 
        WHERE LOWER(name) 
            LIKE '%' || :query || '%' 
            OR UPPER(symbol) == :query
            """
    )
    fun searchCompanyListing(query: String): Flow<List<CompanyListingEntity>>
}