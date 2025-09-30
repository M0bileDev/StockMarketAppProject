@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.example.stockmarketappproject.presentation.screen.companyinfo

import androidx.lifecycle.SavedStateHandle
import com.example.stockmarketappproject.data.model.info.CompanyInfoData
import com.example.stockmarketappproject.data.model.intraday.CompanyIntradayInfoData
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapper
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoPresentation
import com.example.stockmarketappproject.presentation.model.info.CompanyInfoState
import com.example.stockmarketappproject.presentation.model.info.InfoScreenEvents
import com.example.stockmarketappproject.presentation.model.info.ViewModelInfoEvents
import com.example.stockmarketappproject.presentation.model.intraday.CompanyIntradayInfoPresentation
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import com.example.stockmarketappproject.utils.model.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

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

    @Test
    fun givenViewModel_whenCollectCompanyInfo_ResourceError_thenDatabaseErrorIsEmitted() = runTest {
        every { savedStateHandle.get<String>("symbol") } returns ""
        every { companyInfoRepository.getCompanyInfo("") } returns flow {
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

        //when collect company info
        companyInfoViewModel.collectCompanyInfo()

        // then
        job.cancel()
        assertEquals(ViewModelEvents.DatabaseError, action)
    }

    @Test
    fun givenViewModel_whenFetchCompanyInfo_ResourceError_thenDatabaseErrorIsEmitted() = runTest {
        every { savedStateHandle.get<String>("symbol") } returns ""
        coEvery { companyInfoRepository.fetchCompanyInfo("") } returns Resource.Error(
            message = ""
        )
        coEvery {
            intradayRepository.fetchIntradayInfo("")
        } returns Resource.Error(
            message = ""
        )

        lateinit var action: ViewModelEvents
        val job = launch(UnconfinedTestDispatcher()) {
            companyInfoViewModel.event.collect {
                action = it
            }
        }

        //given view model

        //when fetch company info
        companyInfoViewModel.fetchCompanyInfo()

        // then
        job.cancel()
        assertEquals(ViewModelEvents.NetworkError, action)
    }

    @Test
    fun givenViewModel_whenEventIsOnRefresh_thenFetchCompanyInfoIsExecuted() = runTest {
        every { savedStateHandle.get<String>("symbol") } returns ""
        coEvery { companyInfoRepository.fetchCompanyInfo("") } returns Resource.Success(
            CompanyInfoData("", "", "", "", "")
        )
        coEvery {
            intradayRepository.fetchIntradayInfo("")
        } returns Resource.Success(
            listOf(
                CompanyIntradayInfoData(LocalDateTime.now(), 0.0)
            )
        )

        //given viewmodel
        val spykViewModel = spyk(companyInfoViewModel, recordPrivateCalls = true)

        //when
        spykViewModel.onEvent(InfoScreenEvents.OnRefresh)

        //then
        verify { spykViewModel["fetchCompanyInfo"]() }
    }

    @Test
    fun givenViewModel_whenCollectCompanyInfo_thenViewStateIsNotDefault() = runTest {
        every { savedStateHandle.get<String>("symbol") } returns ""
        every { companyInfoRepository.getCompanyInfo("") } returns flow {
            emit(
                Resource.Success(
                    CompanyInfoData("", "", "", "", "")
                )
            )
        }
        every { infoPresentationMapper.run { any<CompanyInfoData>().toPresentation() } } returns CompanyInfoPresentation(
            "",
            "",
            "",
            "",
            ""
        )

        //given viewmodel

        //when collect company info
        companyInfoViewModel.collectCompanyInfo()

        //then
        val actualState = companyInfoViewModel.state
        val matcher = CompanyInfoState.createDefault()

        assertThat(actualState, `is`(not(matcher)))
    }

    @Test
    fun givenViewModel_whenCollectIntradayInfo_thenViewStateIsNotDefault() = runTest {
        every { savedStateHandle.get<String>("symbol") } returns ""
        every { intradayRepository.getIntradayInfo("") } returns flow {
            emit(
                Resource.Success(
                    listOf(
                        CompanyIntradayInfoData(LocalDateTime.now(), 0.0)
                    )
                )
            )
        }
        every { intradayPresentationMapper.run { any<CompanyIntradayInfoData>().toPresentation() } } returns CompanyIntradayInfoPresentation(
            LocalDateTime.now(), 0.0
        )

        //given viewmodel

        //when collect company intraday info
        companyInfoViewModel.collectIntradayInfo()

        //then
        val actualState = companyInfoViewModel.state
        val matcher = CompanyInfoState.createDefault()

        assertThat(actualState, `is`(not(matcher)))
    }

}