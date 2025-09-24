package com.example.stockmarketappproject.presentation.screen.companyinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoState
import com.example.stockmarketappproject.presentation.screen.companylisting.TAG
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val companyInfoRepository: DefaultInfoRepository,
    private val intradayRepository: DefaultIntradayRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyInfoState.createDefault())
    val state get() = _state.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ViewModelEvents>()
    val event get() = _viewModelEvent.asSharedFlow()

    private val name = savedStateHandle.get<String>("name")
    private var fetchInfoJob: Job? = null
    private val fetchInfoScope = CoroutineScope(Dispatchers.IO)

    private var fetchIntradayJob: Job? = null
    private val fetchIntradayScope = CoroutineScope(Dispatchers.IO)

    init {
        fetchCompanyInfo()
        fetchIntradayInfo()
    }

    private fun fetchIntradayInfo() = fetchOnNavigationArgumentOrError(
        navArg = name,
        onFetch = { arg ->
            with(fetchIntradayScope) {
                fetchIntradayJob?.cancel()
                fetchIntradayJob = launch {
                    if (!isActive) return@launch

                    _state.value = _state.value.copy(isRefreshing = true)

                    intradayRepository.fetchIntradayInfo(arg).collect { resource ->
                        when (resource) {
                            is Resource.Error -> {
                                _viewModelEvent.emit(ViewModelEvents.NetworkError)
                                Log.d(TAG, "handle error")
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
            }
        },
        onError = {
            viewModelScope.launch {
                _viewModelEvent.emit(ViewModelEvents.NavigationArgumentError)
            }
        }
    )

    private fun fetchCompanyInfo() = fetchOnNavigationArgumentOrError(
        navArg = name,
        onFetch = { arg ->
            with(fetchInfoScope) {
                fetchInfoJob?.cancel()
                fetchInfoJob = launch {
                    if (!isActive) return@launch

                    _state.value = _state.value.copy(isRefreshing = true)

                    companyInfoRepository.fetchCompanyInfo(arg).collect { resource ->
                        when (resource) {
                            is Resource.Error -> {
                                _viewModelEvent.emit(ViewModelEvents.NetworkError)
                                Log.d(TAG, "handle error")
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
            }
        },
        onError = {
            viewModelScope.launch {
                _viewModelEvent.emit(ViewModelEvents.NavigationArgumentError)
            }
        }
    )


    private fun <T> fetchOnNavigationArgumentOrError(
        navArg: T?,
        onFetch: (T) -> Unit,
        onError: () -> Unit
    ) {
        navArg?.let { arg ->
            onFetch(arg)
        } ?: onError()
    }

    override fun onCleared() {
        super.onCleared()
        fetchInfoJob?.cancel()
        fetchIntradayJob?.cancel()
    }
}