package com.example.stockmarketappproject.data.mappers.info

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.data.remote.model.dto.DtoModel

interface InfoDataMapper<in Dto : DtoModel, out Data : DataModel> {
    fun Dto.toCompanyInfoData(): Data
}