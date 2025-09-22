package com.example.stockmarketappproject.data.parser

import com.example.stockmarketappproject.data.model.DataModel
import java.io.InputStream

interface CsvParser<out T : DataModel> {
    suspend fun parse(stream: InputStream): List<T>
}