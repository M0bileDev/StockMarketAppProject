package com.example.stockmarketappproject.presentation.mapper

import com.example.stockmarketappproject.data.model.DataModel
import com.example.stockmarketappproject.presentation.model.PresentationModel

interface StockPresentationMapper<Data : DataModel, Presentation : PresentationModel> {

    fun Data.toPresentation(): Presentation
}