package com.example.stockmarketappproject.utils.localdatetimeformatter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LocalDateTimeFormatterImpl @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter
) : LocalDateTimeFormatter {

    override fun String.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.parse(this, dateTimeFormatter)
    }

    override fun LocalDateTime.fromToString(): String {
        return this.format(dateTimeFormatter)
    }
}