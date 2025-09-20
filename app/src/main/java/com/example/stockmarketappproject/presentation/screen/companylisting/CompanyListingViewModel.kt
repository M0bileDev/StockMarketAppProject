package com.example.stockmarketappproject.presentation.screen.companylisting

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stockmarketappproject.data.repository.DefaultStockRepository
import com.example.stockmarketappproject.presentation.model.CompanyListingEvent
import com.example.stockmarketappproject.presentation.model.CompanyListingState
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.utils.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "CompanyListingViewModel"

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: DefaultStockRepository
) : ViewModel() {

    init {
        fetchCompanies()
    }

    private var searchJob: Job? = null
    private var fetchJob: Job? = null
    private val searchScope = CoroutineScope(Dispatchers.IO)
    private val fetchScope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(CompanyListingState.createDefault())
    val state get() = _state.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ViewModelEvents>()
    val event get() = _viewModelEvent.asSharedFlow()

    fun onEvent(companyListingEvent: CompanyListingEvent) =
        when (companyListingEvent) {
            is CompanyListingEvent.OnNavigate -> TODO()
            is CompanyListingEvent.OnSearchQueryChange -> ::searchCompany
            is CompanyListingEvent.OnRefresh -> ::fetchCompanies
        }

    private fun fetchCompanies() = with(fetchScope) {
        fetchJob?.cancel()
        fetchJob = launch {
            if (!isActive) return@launch

            _state.value = _state.value.copy(isLoading = true)

            stockRepository.fetchCompanyListing().collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _viewModelEvent.emit(ViewModelEvents.NetworkError)
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "Data has been fetched successfully...")
                    }
                }

                _state.value = _state.value.copy(isLoading = false)
            }
        }

    }

    fun searchCompany(query: String) = with(searchScope) {
        _state.value = _state.value.copy(searchQuery = query)

        searchJob?.cancel()
        searchJob = launch {
            if (!isActive) return@launch

            stockRepository.getCompanyListing(query).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> TODO()
                    is Resource.Success -> TODO()
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        fetchJob?.cancel()
    }

}