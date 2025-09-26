package com.example.stockmarketappproject.data.repository.listing

import com.example.stockmarketappproject.data.local.dao.ListingDao
import com.example.stockmarketappproject.data.mappers.listing.DefaultListingDataMapper
import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.example.stockmarketappproject.data.parser.listing.DefaultCsvListingParser
import com.example.stockmarketappproject.data.remote.api.ListingApi
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ListingRepositoryImpl @Inject constructor(
    private val listingApi: ListingApi,
    private val listingDao: ListingDao,
    private val defaultListingDataMapper: DefaultListingDataMapper,
    private val defaultCsvListingParser: DefaultCsvListingParser
) : DefaultListingRepository {

    override fun getCompanyListing(query: String): Flow<Resource<List<CompanyListingData>>> =
        listingDao.searchCompanyListing(query).transform { companyListingEntities ->
            try {
                val result = with(defaultListingDataMapper) {
                    companyListingEntities.map { entity -> entity.toCompanyListingData() }
                }
                if (result.isEmpty()) throw IllegalStateException("Data is empty")

                emit(Resource.Success(data = result))
            } catch (ise: IllegalStateException) {
                ise.printStackTrace()
                emit(Resource.Error(ise.localizedMessage))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage))
            }
        }

    override suspend fun fetchCompanyListing(): Resource<List<CompanyListingData>> =
        try {
            val response = listingApi.getListings()
            val data = defaultCsvListingParser.parse(response.byteStream())
            val result = with(defaultListingDataMapper) {
                data.map { companyListingData -> companyListingData.toCompanyListingEntity() }
            }
            if (result.isEmpty()) throw IllegalStateException("Data is empty")

            // TODO: think about upsert
            listingDao.clearCompanyListings()
            listingDao.insertCompanyListing(result)
            Resource.Success(data)
        } catch (ise: IllegalStateException) {
            ise.printStackTrace()
            Resource.Error(ise.localizedMessage)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            Resource.Error(ioe.localizedMessage)
        } catch (httpe: HttpException) {
            httpe.printStackTrace()
            Resource.Error(httpe.localizedMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }
}