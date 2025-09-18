package com.example.stockmarketappproject.data.parser

import com.example.stockmarketappproject.domain.model.CompanyListingDomain
import java.io.InputStream

interface CsvParser<out T: CompanyListingDomain> {
    suspend fun parse(stream: InputStream): List<T>
}