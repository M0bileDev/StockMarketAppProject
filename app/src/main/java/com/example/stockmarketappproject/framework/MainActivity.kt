package com.example.stockmarketappproject.framework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.stockmarketappproject.framework.directions.CompanyInfo
import com.example.stockmarketappproject.framework.directions.CompanyListing
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingEvent
import com.example.stockmarketappproject.presentation.model.listing.ViewModelListingEvents
import com.example.stockmarketappproject.presentation.screen.companyinfo.CompanyInfoScreen
import com.example.stockmarketappproject.presentation.screen.companyinfo.CompanyInfoViewModel
import com.example.stockmarketappproject.presentation.screen.companylisting.CompanyListingScreen
import com.example.stockmarketappproject.presentation.screen.companylisting.CompanyListingViewModel
import com.example.stockmarketappproject.presentation.ui.theme.StockMarketAppProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0)
        )
        setContent {
            StockMarketAppProjectTheme {
                StockMarketApp()
            }
        }
    }
}

@Composable
private fun StockMarketApp() {
    // TODO: it doesn't look great, think about refactor
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(CompanyListing) {
            composable<CompanyListing> {
                val lifecycle = LocalLifecycleOwner.current
                val companyListingViewModel: CompanyListingViewModel = hiltViewModel()

                with(companyListingViewModel) {
                    val companyListingState by state.collectAsStateWithLifecycle()

                    LaunchedEffect(lifecycle) {
                        lifecycle.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            event.distinctUntilChanged()
                                .collectLatest { event ->
                                    when {
                                        event is ViewModelListingEvents.NavigateToCompanyInfo -> {
                                            navController.navigate(CompanyInfo(event.name))
                                        }
                                    }
                                }
                        }
                    }

                    // TODO: I think context scope function will work great here, try next
                    CompanyListingScreen(
                        companyListingState = companyListingState,
                        onSearchQueryChanged = { query ->
                            onEvent(
                                CompanyListingEvent.OnSearchQueryChange(
                                    query
                                )
                            )
                        },
                        onRefresh = {
                            onEvent(CompanyListingEvent.OnRefresh)
                        },
                        onNavigateToCompanyInfo = { symbol ->
                            onEvent(CompanyListingEvent.OnNavigate(symbol))
                        }
                    )
                }
            }
            composable<CompanyInfo> {
                val companyInfoViewModel: CompanyInfoViewModel = hiltViewModel()

                with(companyInfoViewModel) {
                    val companyInfoState by state.collectAsStateWithLifecycle()
                    // TODO: I think context scope function will work great here, try next
                    CompanyInfoScreen(companyInfoState = companyInfoState, onRefresh = {
                        // TODO: implement refresh
                    })

                }

            }
        }
    }

    NavHost(
        navController = navController,
        graph = navGraph
    )

}
