package com.example.stockmarketappproject.presentation.screen.companylisting

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.stockmarketappproject.data.repository.DefaultStockRepository
import com.example.stockmarketappproject.presentation.mapper.DefaultStockPresentationMapper
import com.example.stockmarketappproject.presentation.model.CompanyListingEvent
import com.example.stockmarketappproject.presentation.model.CompanyListingState
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.utils.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val stockRepository: DefaultStockRepository,
    private val stockPresentationMapper: DefaultStockPresentationMapper
) : ViewModel() {

    private var searchJob: Job? = null
    private var fetchJob: Job? = null
    private val searchScope = CoroutineScope(Dispatchers.IO)
    private val fetchScope = CoroutineScope(Dispatchers.IO)

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
                        is Resource.Error -> TODO()
                        is Resource.Success -> {
                            val data =
                                resource.data ?: throw IllegalStateException("Data cannot be null")
                            val presentation =
                                data.map {
                                    with(stockPresentationMapper) {
                                        it.toPresentation()
                                    }
                                }
                            _state.value = _state.value.copy(companies = presentation)
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

    fun onEvent(companyListingEvent: CompanyListingEvent) =
        when (companyListingEvent) {
            is CompanyListingEvent.OnNavigate -> TODO()
            is CompanyListingEvent.OnSearchQueryChange -> {
                search = companyListingEvent.query
            }

            is CompanyListingEvent.OnRefresh -> {
                fetchCompanies()
            }
        }

    private fun fetchCompanies() = with(fetchScope) {
        fetchJob?.cancel()
        fetchJob = launch {
            if (!isActive) return@launch

            _state.value = _state.value.copy(isRefreshing = true)

            stockRepository.fetchCompanyListing().collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _viewModelEvent.emit(ViewModelEvents.NetworkError)
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "Data has been fetched successfully...")
                    }
                }

                // TODO: also with state update?
                _state.value = _state.value.copy(isRefreshing = false)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        fetchJob?.cancel()
    }

}