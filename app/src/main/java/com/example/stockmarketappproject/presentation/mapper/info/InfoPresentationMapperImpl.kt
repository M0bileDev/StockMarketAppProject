package com.example.stockmarketappproject.presentation.mapper.info

import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoPresentation
import javax.inject.Inject

class InfoPresentationMapperImpl @Inject constructor() : InfoPresentationMapper {
    override fun CompanyInfoData.toPresentation(): CompanyInfoPresentation {
        return CompanyInfoPresentation(
            symbol,
            description,
            name,
            country,
            industry
        )
    }
}