package com.example.stockmarketappproject.presentation.model.listing

import com.example.stockmarketappproject.presentation.model.ViewModelEvents

interface ViewModelListingEvents : ViewModelEvents {

    data class NavigateToCompanyInfo(val name: String) : ViewModelListingEvents
    data object DismissSnackbar: ViewModelListingEvents
}