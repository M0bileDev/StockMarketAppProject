package com.example.stockmarketappproject.presentation.model

sealed interface CompanyListingEvent {
    object OnRefresh : CompanyListingEvent
    data class OnSearchQueryChange(val query: String) : CompanyListingEvent
    data class OnNavigate(val stockId: Int) : CompanyListingEvent
}