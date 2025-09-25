package com.example.stockmarketappproject.presentation.model.listing

sealed interface CompanyListingEvent {
    object OnRefresh : CompanyListingEvent
    data class OnSearchQueryChange(val query: String) : CompanyListingEvent
    data class OnNavigate(val name:String) : CompanyListingEvent
}