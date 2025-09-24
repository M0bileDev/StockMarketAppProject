package com.example.stockmarketappproject.data.repository.info

import com.example.stockmarketappproject.data.local.dao.InfoDao
import com.example.stockmarketappproject.data.mappers.info.DefaultInfoDataMapper
import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.data.remote.api.InfoApi
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(
    private val infoApi: InfoApi,
    private val infoDao: InfoDao,
    private val defaultInfoDataMapper: DefaultInfoDataMapper
) : DefaultInfoRepository {
    override fun getCompanyInfo(name: String): Flow<Resource<CompanyInfoData>> =
        infoDao.getCompanyInfo(name).transform { companyInfoEntity ->
            try {
                val result = with(defaultInfoDataMapper) {
                    companyInfoEntity.toCompanyInfoData()
                }
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
            }
        }

    override suspend fun fetchCompanyInfo(name: String): Flow<Resource<CompanyInfoData>> {
        return flow {
            try {
                val response = infoApi.getCompanyInfo(name)
                if (response == null) throw IllegalStateException("Data is empty")

                val data = with(defaultInfoDataMapper) {
                    response.toCompanyInfoData()
                }
                val result = with(defaultInfoDataMapper) {
                    data.toCompanyInfoEntity()
                }

                infoDao.deleteCompanyInfo(name)
                infoDao.insertCompanyInfo(result)

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