package com.example.stockmarketappproject.data.mappers.info

import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.data.remote.model.dto.info.CompanyInfoDto
import javax.inject.Inject

class InfoDataMapperImpl @Inject constructor() : DefaultInfoDataMapper {
    override fun CompanyInfoDto.toCompanyInfoData(): CompanyInfoData {
        return CompanyInfoData(
            symbol,
            description,
            name,
            country,
            industry
        )
    }
}