@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.example.stockmarketappproject.presentation.screen.companyinfo

import androidx.lifecycle.SavedStateHandle
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapper
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.info.ViewModelInfoEvents
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import com.example.stockmarketappproject.utils.model.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CompanyInfoViewModelTest {

    lateinit var companyInfoViewModel: CompanyInfoViewModel
    val savedStateHandle = mockk<SavedStateHandle>()
    private val companyInfoRepository = mockk<DefaultInfoRepository>()
    private val intradayRepository = mockk<DefaultIntradayRepository>()
    private val infoPresentationMapper = mockk<InfoPresentationMapper>()
    private val intradayPresentationMapper = mockk<IntradayPresentationMapper>()
    private val dispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
        override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
        override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
    }

    @Before
    fun setup() {
        companyInfoViewModel = CompanyInfoViewModel(
            savedStateHandle,
            companyInfoRepository,
            intradayRepository,
            infoPresentationMapper,
            intradayPresentationMapper,
            dispatcherProvider
        )
    }

    @Test
    fun givenViewModel_whenNavArgIsNull_collectCompanyInfo_thenNavigationArgumentErrorIsEmitted() {
        val mainThreadSurrogate = newSingleThreadContext("UI thread")
        Dispatchers.setMain(mainThreadSurrogate)

        //no deadlock between runBlocking and Dispatchers.Main
        runBlocking(mainThreadSurrogate) {
            //given viewModel

            //when nav arg is null and collect intraday info
            every { savedStateHandle.get<String>("symbol") } returns null
            companyInfoViewModel.collectCompanyInfo()

            //then action is NoLocationData
            val action = companyInfoViewModel.event.first()
            assertEquals(ViewModelInfoEvents.NavigationArgumentError, action)
        }

        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun givenViewModel_whenNavArgIsNull_collectIntradayInfo_thenNavigationArgumentErrorIsEmitted() {
        val mainThreadSurrogate = newSingleThreadContext("UI thread")
        Dispatchers.setMain(mainThreadSurrogate)

        //no deadlock between runBlocking and Dispatchers.Main
        runBlocking(mainThreadSurrogate) {
            //given viewModel

            //when nav arg is null and collect intraday info
            every { savedStateHandle.get<String>("symbol") } returns null
            companyInfoViewModel.collectIntradayInfo()

            //then action is NoLocationData
            val action = companyInfoViewModel.event.first()
            assertEquals(ViewModelInfoEvents.NavigationArgumentError, action)
        }

        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun givenViewModel_whenNavArgIsNull_fetchCompanyInfo_thenNavigationArgumentErrorIsEmitted() {
        val mainThreadSurrogate = newSingleThreadContext("UI thread")
        Dispatchers.setMain(mainThreadSurrogate)

        //no deadlock between runBlocking and Dispatchers.Main
        runBlocking(mainThreadSurrogate) {
            //given viewModel

            //when nav arg is null and collect intraday info
            every { savedStateHandle.get<String>("symbol") } returns null
            companyInfoViewModel.fetchCompanyInfo()

            //then action is NoLocationData
            val action = companyInfoViewModel.event.first()
            assertEquals(ViewModelInfoEvents.NavigationArgumentError, action)
        }

        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun givenViewModel_whenCollectIntradayInfo_ResourceError_thenDatabaseErrorIsEmitted() =
        runTest {
            every { savedStateHandle.get<String>("symbol") } returns ""
            every { intradayRepository.getIntradayInfo("") } returns flow {
                emit(
                    Resource.Error(
                        message = ""
                    )
                )
            }

            lateinit var action: ViewModelEvents
            val job = launch(UnconfinedTestDispatcher()) {
                companyInfoViewModel.event.collect {
                    action = it
                }
            }

            //given view model

            //when collect intraday info
            companyInfoViewModel.collectIntradayInfo()

            // then
            job.cancel()
            assertEquals(ViewModelEvents.DatabaseError, action)
        }

}