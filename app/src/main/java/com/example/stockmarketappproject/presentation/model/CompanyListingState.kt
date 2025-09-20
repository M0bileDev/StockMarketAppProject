package com.example.stockmarketappproject.presentation.model

data class CompanyListingState(
    val companies: List<CompanyListingPresentation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
) {
    companion object {
        fun createDefault(): CompanyListingState = CompanyListingState()
    }
}