package com.example.stockmarketappproject.data.repository.intraday

import com.example.stockmarketappproject.data.local.dao.IntradayDao
import com.example.stockmarketappproject.data.mappers.intraday.DefaultIntradayDataMapper
import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.parser.intraday.DefaultCsvIntradayParser
import com.example.stockmarketappproject.data.remote.api.IntradayApi
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class IntradayRepositoryImpl @Inject constructor(
    private val intradayApi: IntradayApi,
    private val intradayDao: IntradayDao,
    private val defaultIntradayDataMapper: DefaultIntradayDataMapper,
    private val defaultCsvIntradayParser: DefaultCsvIntradayParser,
    private val dispatcherProvider: DispatcherProvider
) : DefaultIntradayRepository {

    override fun getIntradayInfo(query: String): Flow<Resource<List<CompanyIntradayInfoData>>> =
        intradayDao.getCompanyIntradayInfoEntities(query).transform { companyIntradayInfoEntities ->
            try {
                val result = with(defaultIntradayDataMapper) {
                    companyIntradayInfoEntities.map { entity -> entity.toCompanyIntradayInfoData() }
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

    override suspend fun fetchIntradayInfo(symbol: String): Resource<List<CompanyIntradayInfoData>> =
        withContext(dispatcherProvider.io) {
            try {
                val response = intradayApi.getIntradayInfo(symbol)
                val data = defaultCsvIntradayParser.parse(response.byteStream())
                val result = with(defaultIntradayDataMapper) {
                    data.map { companyIntradayInfoData ->
                        companyIntradayInfoData.toCompanyIntradayInfoEntity(
                            symbol
                        )
                    }
                }
                if (result.isEmpty()) throw IllegalStateException("Data is empty")

                //todo logically when company listing was deleted, intraday info also has to be deleted (all info)
                // TODO: probably add trigger on db
                with(intradayDao) {
                    deleteCompanyIntradayInfo(symbol)
                    insertCompanyIntradayInfo(result)
                }

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

}