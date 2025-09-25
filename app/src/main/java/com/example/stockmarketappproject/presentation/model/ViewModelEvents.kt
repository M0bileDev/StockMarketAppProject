package com.example.stockmarketappproject.presentation.model

interface ViewModelEvents {
    object NetworkError : ViewModelEvents
    object DatabaseError : ViewModelEvents
}