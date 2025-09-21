package com.example.stockmarketappproject.presentation.mapper

interface StockPresentationMapper<Data, Presentation> {

    fun Data.toPresentation(): Presentation
}