package com.example.stockmarketappproject.data.repository

import com.example.stockmarketappproject.data.local.dao.StockDao
import com.example.stockmarketappproject.data.mappers.StockMapper
import com.example.stockmarketappproject.data.model.CompanyListingData
import com.example.stockmarketappproject.data.parser.CsvParser
import com.example.stockmarketappproject.data.remote.api.StockApi
import com.example.stockmarketappproject.domain.model.CompanyListingDomain
import com.example.stockmarketappproject.domain.repository.StockRepository
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao,
    private val stockMapper: StockMapper,
    private val csvParser: CsvParser<CompanyListingData>
) : StockRepository {

//    override suspend fun getCompanyListing(query: String): Flow<Resource<List<CompanyListingDomain>>> {
//        return flow {
//            try {
//                val result = stockDao.searchCompanyListing(query)
//                    .map { companyListingEntity ->
//                        with(stockMapper) {
//                            companyListingEntity.toCompanyListingData()
//                        }
//                    }
//                emit(Resource.Success(result))
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emit(Resource.Error(message = e.localizedMessage))
//            }
//        }
//    }

    override fun getCompanyListing(query: String): Flow<Resource<List<CompanyListingDomain>>> =
        stockDao.searchCompanyListing(query).transform { companyListingEntities ->
            try {
                val result = with(stockMapper) {
                    companyListingEntities.map { entity -> entity.toCompanyListingData() }
                }
                Resource.Success(data = result)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }

    override suspend fun fetchCompanyListing(): Resource<List<CompanyListingDomain>> {
        return try {
            val response = stockApi.getListings()
            val data = csvParser.parse(response.byteStream())
            Resource.Success(data)
        } catch (ioe: IOException) {
            Resource.Error(ioe.localizedMessage)
        } catch (httpe: HttpException) {
            Resource.Error(httpe.localizedMessage)
        }
    }


}