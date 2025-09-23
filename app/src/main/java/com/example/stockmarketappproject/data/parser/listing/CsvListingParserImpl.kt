package com.example.stockmarketappproject.data.parser.listing

import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject


class CsvListingParserImpl @Inject constructor() : DefaultCsvListingParser {

    override suspend fun parse(stream: InputStream): List<CompanyListingData> {
        val reader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            reader.use {
                it.readAll()
                    //Skip headers
                    .drop(1)
                    .mapNotNull { row ->
                        val symbol = row.getOrNull(0)
                        val name = row.getOrNull(1)
                        val exchange = row.getOrNull(2)

                        CompanyListingData(
                            //Return null, then map will skip this entry
                            symbol = symbol ?: return@mapNotNull null,
                            name = name ?: return@mapNotNull null,
                            exchange = exchange ?: return@mapNotNull null
                        )
                    }
            }
        }
    }

}