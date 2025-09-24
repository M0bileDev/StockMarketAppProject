package com.example.stockmarketappproject.presentation.mapper

import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingPresentation

interface DefaultStockPresentationMapper :
    StockPresentationMapper<CompanyListingData, CompanyListingPresentation>