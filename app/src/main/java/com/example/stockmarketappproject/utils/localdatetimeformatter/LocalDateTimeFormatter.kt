package com.example.stockmarketappproject.utils.localdatetimeformatter

import java.time.LocalDateTime

interface LocalDateTimeFormatter {
    fun String.toLocalDateTime() : LocalDateTime
    fun LocalDateTime.fromToString(): String
}