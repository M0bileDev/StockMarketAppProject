package com.example.stockmarketappproject.presentation.model.listing

sealed interface ListingScreenEvent {
    object OnRefresh : ListingScreenEvent
    data class OnSearchQueryChange(val query: String) : ListingScreenEvent
    data class OnNavigate(val name:String) : ListingScreenEvent
}