package com.example.stockmarketappproject.presentation.model.info

import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.listing.ViewModelListingEvents

interface ViewModelInfoEvents : ViewModelEvents {
    object NavigationArgumentError : ViewModelListingEvents
}