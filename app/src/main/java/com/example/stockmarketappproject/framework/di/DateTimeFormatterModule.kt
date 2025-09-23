package com.example.stockmarketappproject.framework.di

import com.example.stockmarketappproject.utils.localdatetimeformatter.LocalDateTimeFormatter
import com.example.stockmarketappproject.utils.localdatetimeformatter.LocalDateTimeFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.format.DateTimeFormatter
import javax.inject.Named
import javax.inject.Singleton

const val DATE_TIME_FORMATTER_PATTERN = "DATE_TIME_FORMATTER_PATTERN"

@[Module InstallIn(SingletonComponent::class)]
abstract class DateTimeFormatterBinder {

    @[Binds Singleton]
    abstract fun bindLocalDateTimeFormatter(localDateTimeFormatterImpl: LocalDateTimeFormatterImpl): LocalDateTimeFormatter
}

@[Module InstallIn(SingletonComponent::class)]
object DateTimeFormatterProvider {

    @[Provides Singleton Named(DATE_TIME_FORMATTER_PATTERN)]
    fun providePattern(): String {
        return "yyyy-MM-dd HH:mm:ss"
    }

    @[Provides Singleton]
    fun provideDateTimeFormatter(
        @Named(DATE_TIME_FORMATTER_PATTERN) pattern: String
    ): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(pattern)
    }
}