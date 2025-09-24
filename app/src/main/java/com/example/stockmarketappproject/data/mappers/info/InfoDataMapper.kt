package com.example.stockmarketappproject.data.mappers.info

import com.example.stockmarketappproject.data.local.entity.EntityModel
import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.data.remote.model.dto.DtoModel

interface InfoDataMapper<in Dto : DtoModel, Data : DataModel, Entity : EntityModel> {
    fun Dto.toCompanyInfoData(): Data
    fun Data.toCompanyInfoEntity(): Entity
    fun Entity.toCompanyInfoData(): Data
}