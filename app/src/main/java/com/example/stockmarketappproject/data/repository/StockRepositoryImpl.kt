package com.example.stockmarketappproject.data.repository

import com.example.stockmarketappproject.data.local.dao.StockDao
import com.example.stockmarketappproject.data.mappers.DefaultStockDataMapper
import com.example.stockmarketappproject.data.model.CompanyListingData
import com.example.stockmarketappproject.data.parser.DefaultCsvParser
import com.example.stockmarketappproject.data.remote.api.StockApi
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao,
    private val stockDataMapper: DefaultStockDataMapper,
    private val csvParser: DefaultCsvParser
) : DefaultStockRepository {

    override fun getCompanyListing(query: String): Flow<Resource<List<CompanyListingData>>> =
        stockDao.searchCompanyListing(query).transform { companyListingEntities ->
            try {
                val result = with(stockDataMapper) {
                    companyListingEntities.map { entity -> entity.toCompanyListingData() }
                }
                Resource.Success(data = result)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }

    override suspend fun fetchCompanyListing(): Flow<Resource<List<CompanyListingData>>> {
        return flow {
            try {
                val response = stockApi.getListings()
                val data = csvParser.parse(response.byteStream())
                val result = with(stockDataMapper) {
                    data.map { companyListingData -> companyListingData.toCompanyListingEntity() }
                }
                // TODO: think about upsert
                stockDao.clearCompanyListings()
                stockDao.insertCompanyListing(result)
                emit(Resource.Success(data))
            } catch (ioe: IOException) {
                emit(Resource.Error(ioe.localizedMessage))
            } catch (httpe: HttpException) {
                emit(Resource.Error(httpe.localizedMessage))
            }
        }
    }


}