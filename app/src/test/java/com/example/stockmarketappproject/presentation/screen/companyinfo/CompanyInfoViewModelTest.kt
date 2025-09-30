@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.example.stockmarketappproject.presentation.screen.companyinfo

import androidx.lifecycle.SavedStateHandle
import com.example.stockmarketappproject.data.repository.info.DefaultInfoRepository
import com.example.stockmarketappproject.data.repository.intraday.DefaultIntradayRepository
import com.example.stockmarketappproject.presentation.mapper.info.InfoPresentationMapper
import com.example.stockmarketappproject.presentation.mapper.intraday.IntradayPresentationMapper
import com.example.stockmarketappproject.utils.dispatcherprovider.DispatcherProvider
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before

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

}