package com.example.stockmarketappproject.data.repository

import com.example.stockmarketappproject.data.model.CompanyListingData
import com.example.stockmarketappproject.domain.repository.StockRepository

interface DefaultStockRepository : StockRepository<CompanyListingData>