package com.example.stockmarketappproject.data.mappers.intraday

import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.remote.model.dto.intraday.CompanyIntradayInfoDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class IntradayDataMapperImpl @Inject constructor() : DefaultIntradayDataMapper {
    override fun CompanyIntradayInfoDto.toCompanyIntradayInfoData(): CompanyIntradayInfoData {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(timestamp, formatter)

        return CompanyIntradayInfoData(
            timestamp = localDateTime,
            close = close
        )
    }

}