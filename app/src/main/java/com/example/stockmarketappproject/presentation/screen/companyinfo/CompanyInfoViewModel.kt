package com.example.stockmarketappproject.presentation.screen.companyinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapper
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.info.InfoScreenEvents
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoState
import com.example.stockmarketappproject.presentation.model.info.ViewModelInfoEvents
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import com.example.stockmarketappproject.utils.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyInfoRepository: DefaultInfoRepository,
    private val intradayRepository: DefaultIntradayRepository,
    private val infoPresentationMapper: InfoPresentationMapper,
    private val intradayPresentationMapper: IntradayPresentationMapper,
    dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyInfoState.createDefault())
    val state get() = _state.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ViewModelEvents>()
    val event get() = _viewModelEvent.asSharedFlow()

    private val symbol = savedStateHandle.get<String>("symbol")

    private var fetchJob: Job? = null
    private val fetchScope = CoroutineScope(dispatcherProvider.io)

    private var collectInfoJob: Job? = null
    private val collectInfoScope = CoroutineScope(dispatcherProvider.io)

    private var collectIntradayJob: Job? = null
    private val collectIntradayScope = CoroutineScope(dispatcherProvider.io)

    init {
        // TODO: possible combine could work better
        collectCompanyInfo()
        collectIntradayInfo()
        fetchCompanyInfo()
    }

    fun onEvent(infoScreenEvents: InfoScreenEvents) =
        when (infoScreenEvents) {
            is InfoScreenEvents.OnRefresh -> {
                fetchCompanyInfo()
            }
        }

    private fun collectIntradayInfo() =
        actionOnNavigationArgumentOrError(
            symbol,
            onAction = { arg ->
                with(collectIntradayScope) {
                    collectIntradayJob?.cancel()
                    collectIntradayJob = launch {
                        if (!isActive) return@launch

                        intradayRepository.getIntradayInfo(arg).collectLatest { resource ->
                            when (resource) {
                                is Resource.Error -> {
                                    _viewModelEvent.emit(ViewModelEvents.DatabaseError)
                                }

                                is Resource.Success -> {
                                    val data = resource.successData
                                    val presentation = with(intradayPresentationMapper) {
                                        data.map { data -> data.toPresentation() }
                                    }
                                    _state.value = _state.value.copy(stockInfoList = presentation)
                                    _viewModelEvent.emit(ViewModelInfoEvents.DismissSnackbar)
                                }
                            }

                        }
                    }
                }
            }, onError = {
                emitNavigationArgumentError()
            })

    private fun collectCompanyInfo() =
        actionOnNavigationArgumentOrError(
            symbol,
            onAction = { arg ->
                with(collectInfoScope) {
                    collectInfoJob?.cancel()
                    collectInfoJob = launch {
                        if (!isActive) return@launch

                        companyInfoRepository.getCompanyInfo(arg).collectLatest { resource ->
                            when (resource) {
                                is Resource.Error -> {
                                    _viewModelEvent.emit(ViewModelEvents.DatabaseError)
                                }

                                is Resource.Success -> {
                                    val data = resource.successData
                                    val presentation = with(infoPresentationMapper) {
                                        data.toPresentation()
                                    }
                                    _state.value = _state.value.copy(company = presentation)
                                    _viewModelEvent.emit(ViewModelInfoEvents.DismissSnackbar)
                                }
                            }
                        }
                    }
                }
            }, onError = {
                emitNavigationArgumentError()
            })

    private fun fetchCompanyInfo() = actionOnNavigationArgumentOrError(
        navArg = symbol,
        onAction = { arg ->
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

                                is Resource.Success -> {}
                            }
                        },
                        onResult2 = { resource ->
                            when (resource) {
                                is Resource.Error -> {
                                    _viewModelEvent.emit(ViewModelEvents.NetworkError)
                                }

                                is Resource.Success -> {}
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
            emitNavigationArgumentError()
        }
    )

    private fun emitNavigationArgumentError() {
        viewModelScope.launch {
            _viewModelEvent.emit(ViewModelInfoEvents.NavigationArgumentError)
        }
    }


    suspend fun <R1, R2> mergeDeferred(
        result1: Resource<R1>,
        result2: Resource<R2>,
        onResult1: suspend (Resource<R1>) -> Unit,
        onResult2: suspend (Resource<R2>) -> Unit
    ) {
        onResult1(result1)
        onResult2(result2)
    }

    fun <NavArg> actionOnNavigationArgumentOrError(
        navArg: NavArg?,
        onAction: (NavArg) -> Unit,
        onError: () -> Unit
    ) {
        navArg?.let { arg ->
            onAction(arg)
        } ?: onError()
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
        collectInfoJob?.cancel()
    }
}