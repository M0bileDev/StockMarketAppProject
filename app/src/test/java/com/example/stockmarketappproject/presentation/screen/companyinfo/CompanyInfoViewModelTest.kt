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
import kotlinx.coroutines.delay
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

    private fun usePositivePath() {
        every { savedStateHandle.get<String>("symbol") } returns ""
        every { companyInfoRepository.getCompanyInfo("") } returns flow {
            emit(
                Resource.Success(
                    CompanyInfoData("", "", "", "", "")
                )
            )
        }
        every { intradayRepository.getIntradayInfo("") } returns flow {
            emit(
                Resource.Success(
                    listOf(
                        CompanyIntradayInfoData(LocalDateTime.now(), 0.0)
                    )
                )
            )
        }
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
        every { infoPresentationMapper.run { any<CompanyInfoData>().toPresentation() } } returns CompanyInfoPresentation(
            "",
            "",
            "",
            "",
            ""
        )
        every { intradayPresentationMapper.run { any<CompanyIntradayInfoData>().toPresentation() } } returns CompanyIntradayInfoPresentation(
            LocalDateTime.now(), 0.0
        )
    }

    private fun useNegativePath() {
        every { savedStateHandle.get<String>("symbol") } returns null
        every { companyInfoRepository.getCompanyInfo("") } returns flow {
            emit(
                Resource.Error(
                    message = ""
                )
            )
        }
        every { intradayRepository.getIntradayInfo("") } returns flow {
            emit(
                Resource.Error(
                    message = ""
                )
            )
        }
        coEvery { companyInfoRepository.fetchCompanyInfo("") } returns Resource.Error(
            message = ""
        )
        coEvery {
            intradayRepository.fetchIntradayInfo("")
        } returns Resource.Error(
            message = ""
        )
        every { infoPresentationMapper.run { any<CompanyInfoData>().toPresentation() } } returns CompanyInfoPresentation(
            "",
            "",
            "",
            "",
            ""
        )
        every { intradayPresentationMapper.run { any<CompanyIntradayInfoData>().toPresentation() } } returns CompanyIntradayInfoPresentation(
            LocalDateTime.now(), 0.0
        )
    }

}