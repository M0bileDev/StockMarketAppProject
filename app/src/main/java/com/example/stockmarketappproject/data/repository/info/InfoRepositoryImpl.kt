package com.example.stockmarketappproject.data.repository.info

import com.example.stockmarketappproject.data.local.dao.InfoDao
import com.example.stockmarketappproject.data.mappers.info.DefaultInfoDataMapper
import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.data.remote.api.InfoApi
import com.example.stockmarketappproject.utils.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(
    private val infoApi: InfoApi,
    private val infoDao: InfoDao,
    private val defaultInfoDataMapper: DefaultInfoDataMapper
) : DefaultInfoRepository {
    override fun getCompanyInfo(name: String): Flow<Resource<CompanyInfoData?>> =
        infoDao.getCompanyInfo(name).transform { companyInfoEntity ->
            try {

                if (companyInfoEntity == null) throw IllegalStateException("Data is empty")

                val result = with(defaultInfoDataMapper) {
                    companyInfoEntity.toCompanyInfoData()
                }

                emit(Resource.Success(result))
            } catch (ise: IllegalStateException) {
                ise.printStackTrace()
                emit(Resource.Error(ise.localizedMessage))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage))
            }
        }

    override suspend fun fetchCompanyInfo(name: String): Resource<CompanyInfoData> =
        withContext(Dispatchers.IO) {
            try {
                val response = infoApi.getCompanyInfo(name)
                if (response == null) throw IllegalStateException("Data is empty")

                val data = with(defaultInfoDataMapper) {
                    response.toCompanyInfoData()
                }
                val result = with(defaultInfoDataMapper) {
                    data.toCompanyInfoEntity()
                }

                with(infoDao) {
                    deleteCompanyInfo(name)
                    insertCompanyInfo(result)
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