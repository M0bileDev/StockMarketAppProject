package com.example.stockmarketappproject.utils.model

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(val successData: T) : Resource<T>(data = successData)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}