package com.example.stockmarketappproject.presentation.model

sealed interface ViewModelEvents {
    object NetworkError : ViewModelEvents
}