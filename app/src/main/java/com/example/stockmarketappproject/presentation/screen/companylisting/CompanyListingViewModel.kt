package com.example.stockmarketappproject.presentation.screen.companylisting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketappproject.data.repository.listing.DefaultListingRepository
import com.example.stockmarketappproject.presentation.mapper.listing.ListingPresentationMapper
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.listing.ListingScreenEvent
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingState
import com.example.stockmarketappproject.presentation.model.listing.ViewModelListingEvents
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import com.example.stockmarketappproject.utils.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

const val TAG = "CompanyListingViewModel"

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: DefaultListingRepository,
    private val listingPresentationMapper: ListingPresentationMapper,
    dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var searchJob: Job? = null
    private var fetchJob: Job? = null
    private val searchScope = CoroutineScope(dispatcherProvider.io)
    private val fetchScope = CoroutineScope(dispatcherProvider.io)

    private val _state = MutableStateFlow(CompanyListingState.createDefault())
    val state get() = _state.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ViewModelEvents>()
    val event get() = _viewModelEvent.asSharedFlow()

    private var search by Delegates.observable(initialValue = "") { property, oldValue, newValue ->

        with(searchScope) {
            _state.value = _state.value.copy(searchQuery = newValue)

            searchJob?.cancel()
            searchJob = launch {
                if (!isActive) return@launch

                //provide simple debounce mechanism
                delay(500)

                stockRepository.getCompanyListing(newValue).collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _viewModelEvent.emit(ViewModelEvents.DatabaseError)
                        }

                        is Resource.Success -> {
                            try {
                                val data = resource.successData
                                val presentation =
                                    data.map {
                                        with(listingPresentationMapper) {
                                            it.toPresentation()
                                        }
                                    }
                                _state.value = _state.value.copy(companies = presentation)
                                _viewModelEvent.emit(ViewModelListingEvents.DismissSnackbar)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                _viewModelEvent.emit(ViewModelEvents.DatabaseError)
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        //init delegate
        search = ""
        fetchCompanies()
    }

    fun onEvent(listingScreenEvent: ListingScreenEvent) =
        when (listingScreenEvent) {
            is ListingScreenEvent.OnNavigate -> {
                navigateToCompanyInfo(listingScreenEvent.name)
            }

            is ListingScreenEvent.OnSearchQueryChange -> {
                search = listingScreenEvent.query
            }

            is ListingScreenEvent.OnRefresh -> {
                fetchCompanies()
            }
        }

    private fun navigateToCompanyInfo(name: String) =
        viewModelScope.launch {
            if (!isActive) return@launch
            _viewModelEvent.emit(
                ViewModelListingEvents.NavigateToCompanyInfo(
                    name
                )
            )
        }


    private fun fetchCompanies() = with(fetchScope) {
        fetchJob?.cancel()
        fetchJob = launch {
            if (!isActive) return@launch

            _state.value = _state.value.copy(isRefreshing = true)

            val result = stockRepository.fetchCompanyListing()
            when (result) {
                is Resource.Error -> {
                    _viewModelEvent.emit(ViewModelEvents.NetworkError)
                }

                is Resource.Success -> {
                    Log.d(TAG, "Data has been fetched successfully...")
                }
            }

            // TODO: also with state update?
            //odd situation when result might be so quick that ui thread dont have enough
            // time to redraw, especially pull to refresh animation
            delay(500)
            _state.value = _state.value.copy(isRefreshing = false)

        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        fetchJob?.cancel()
    }

}