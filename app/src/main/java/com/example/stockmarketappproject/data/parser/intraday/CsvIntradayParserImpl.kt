package com.example.stockmarketappproject.data.parser.intraday

import com.example.stockmarketappproject.data.mappers.intraday.DefaultIntradayDataMapper
import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.remote.model.dto.intraday.CompanyIntradayInfoDto
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class CsvIntradayParserImpl @Inject constructor(
    private val defaultIntradayDataMapper: DefaultIntradayDataMapper
) : DefaultCsvIntradayParser {

    override suspend fun parse(stream: InputStream): List<CompanyIntradayInfoData> {
        val reader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            reader.use {
                it.readAll()
                    //Skip headers
                    .drop(1)
                    .mapNotNull { row ->
                        val timestamp = row.getOrNull(0)
                        val close = row.getOrNull(4)

                        with(defaultIntradayDataMapper) {
                            CompanyIntradayInfoDto(
                                //Return null, then map will skip this entry
                                timestamp = timestamp ?: return@mapNotNull null,
                                close = close?.toDouble() ?: return@mapNotNull null,
                            ).toCompanyIntradayInfoData()
                        }

                    }
                    // entries may have different date
                    // like stock market end at previous day 2025.01.31 19:35
                    // and starts next day 2025.02.01 05:40
                    //
                    // business logic -> focus only on previous day
                    .filter { filterData ->
                        filterData.timestamp.dayOfMonth == LocalDateTime.now()
                            .minusDays(1).dayOfMonth
                    }.sortedBy { sortData ->
                        sortData.timestamp.hour
                    }
            }
        }
    }
}