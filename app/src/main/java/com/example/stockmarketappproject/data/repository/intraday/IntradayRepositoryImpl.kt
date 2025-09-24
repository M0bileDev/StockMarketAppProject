package com.example.stockmarketappproject.data.repository.intraday

import com.example.stockmarketappproject.data.local.dao.IntradayDao
import com.example.stockmarketappproject.data.mappers.intraday.DefaultIntradayDataMapper
import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.parser.intraday.DefaultCsvIntradayParser
import com.example.stockmarketappproject.data.remote.api.IntradayApi
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class IntradayRepositoryImpl @Inject constructor(
    private val intradayApi: IntradayApi,
    private val intradayDao: IntradayDao,
    private val defaultIntradayDataMapper: DefaultIntradayDataMapper,
    private val defaultCsvIntradayParser: DefaultCsvIntradayParser
) : DefaultIntradayRepository {

    override fun getIntradayInfo(name: String): Flow<Resource<List<CompanyIntradayInfoData>>> =
        intradayDao.getCompanyIntradayInfoEntities(name).transform { companyIntradayInfoEntities ->
            try {
                val result = with(defaultIntradayDataMapper) {
                    companyIntradayInfoEntities.map { entity -> entity.toCompanyIntradayInfoData() }
                }
                emit(Resource.Success(data = result))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }

    override fun fetchIntradayInfo(name: String): Flow<Resource<List<CompanyIntradayInfoData>>> {
        return flow {
            try {
                val response = intradayApi.getIntradayInfo(name)
                val data = defaultCsvIntradayParser.parse(response.byteStream())
                val result = with(defaultIntradayDataMapper) {
                    data.map { companyIntradayInfoData ->
                        companyIntradayInfoData.toCompanyIntradayInfoEntity(
                            name
                        )
                    }
                }
                if (result.isEmpty()) throw IllegalStateException("Data is empty")

                //todo logically when company listing was deleted, intraday info also has to be deleted (all info)
                intradayDao.deleteCompanyIntradayInfo(name)
                intradayDao.insertCompanyIntradayInfo(result)
                emit(Resource.Success(data))
            } catch (ise: IllegalStateException) {
                emit(Resource.Error(ise.localizedMessage))
            } catch (ioe: IOException) {
                emit(Resource.Error(ioe.localizedMessage))
            } catch (httpe: HttpException) {
                emit(Resource.Error(httpe.localizedMessage))
            }
        }
    }

}