package com.example.stockmarketappproject.data.mappers.intraday

import com.example.stockmarketappproject.data.local.entity.CompanyIntradayInfoEntity
import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.remote.model.dto.intraday.CompanyIntradayInfoDto
import com.example.stockmarketappproject.utils.localdatetimeformatter.LocalDateTimeFormatter
import javax.inject.Inject

class IntradayDataMapperImpl @Inject constructor(
    private val localDateTimeFormatter: LocalDateTimeFormatter
) : DefaultIntradayDataMapper {
    override fun CompanyIntradayInfoDto.toCompanyIntradayInfoData(): CompanyIntradayInfoData {
        val localDateTime = with(localDateTimeFormatter) {
            timestamp.toLocalDateTime()
        }

        return CompanyIntradayInfoData(
            timestamp = localDateTime,
            close = close
        )
    }

    override fun CompanyIntradayInfoData.toCompanyIntradayInfoEntity(symbol: String): CompanyIntradayInfoEntity {
        val timeStamp = with(localDateTimeFormatter) {
            timestamp.fromToString()
        }

        return CompanyIntradayInfoEntity(
            symbol = symbol,
            timestamp = timeStamp,
            close = close
        )
    }

    override fun CompanyIntradayInfoEntity.toCompanyIntradayInfoData(): CompanyIntradayInfoData {
        val localDateTime = with(localDateTimeFormatter) {
            timestamp.toLocalDateTime()
        }
        return CompanyIntradayInfoData(
            timestamp = localDateTime,
            close = close
        )
    }

}