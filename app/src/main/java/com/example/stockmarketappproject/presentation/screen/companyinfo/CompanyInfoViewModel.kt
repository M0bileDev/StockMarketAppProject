package com.example.stockmarketappproject.presentation.screen.companyinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoState
import com.example.stockmarketappproject.presentation.model.info.ViewModelInfoEvents
import com.example.stockmarketappproject.utils.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "CompanyInfoViewModel"

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyInfoRepository: DefaultInfoRepository,
    private val intradayRepository: DefaultIntradayRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyInfoState.createDefault())
    val state get() = _state.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ViewModelEvents>()
    val event get() = _viewModelEvent.asSharedFlow()

    private val name = savedStateHandle.get<String>("name")

    private var fetchJob: Job? = null
    private val fetchScope = CoroutineScope(Dispatchers.IO)

    init {
        fetch()
    }

    private fun fetch() = fetchOnNavigationArgumentOrError(
        navArg = name,
        onFetch = { arg ->
            with(fetchScope) {
                fetchJob?.cancel()
                fetchJob = launch {
                    if (!isActive) return@launch

                    _state.value = _state.value.copy(isRefreshing = true)

                    val infoResult = async { companyInfoRepository.fetchCompanyInfo(arg) }
                    val intradayResult = async { intradayRepository.fetchIntradayInfo(arg) }

                    mergeDeferred(
                        infoResult.await(),
                        intradayResult.await(),
                        onResult1 = { resource ->
                            when (resource) {
                                is Resource.Error -> {
                                    _viewModelEvent.emit(ViewModelEvents.NetworkError)
                                }

                                is Resource.Success -> {
                                    Log.d(
                                        TAG,
                                        "Data has been fetched successfully..."
                                    )
                                }
                            }
                        },
                        onResult2 = { resource ->
                            when (resource) {
                                is Resource.Error -> {
                                    _viewModelEvent.emit(ViewModelEvents.NetworkError)
                                }

                                is Resource.Success -> {
                                    Log.d(
                                        TAG,
                                        "Data has been fetched successfully..."
                                    )
                                }
                            }
                        }
                    )

                    //odd situation when result might be so quick that ui thread dont have enough
                    // time to redraw, especially pull to refresh animation
                    delay(500)
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        },
        onError = {
            viewModelScope.launch {
                _viewModelEvent.emit(ViewModelInfoEvents.NavigationArgumentError)
            }
        }
    )


    suspend fun <R1, R2> mergeDeferred(
        result1: Resource<R1>,
        result2: Resource<R2>,
        onResult1: suspend (Resource<R1>) -> Unit,
        onResult2: suspend (Resource<R2>) -> Unit
    ) {
        onResult1(result1)
        onResult2(result2)
    }

    private fun <NavArg> fetchOnNavigationArgumentOrError(
        navArg: NavArg?,
        onFetch: (NavArg) -> Unit,
        onError: () -> Unit
    ) {
        navArg?.let { arg ->
            onFetch(arg)
        } ?: onError()
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}