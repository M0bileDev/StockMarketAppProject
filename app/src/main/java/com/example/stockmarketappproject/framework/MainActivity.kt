package com.example.stockmarketappproject.framework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.stockmarketappproject.R
import com.example.stockmarketappproject.framework.directions.CompanyInfo
import com.example.stockmarketappproject.framework.directions.CompanyListing
import com.example.stockmarketappproject.presentation.model.ViewModelEvents
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
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(CompanyListing) {
            companyListingComposable(navGraphBuilder = this, navController = navController)
            companyInfoComposable(navGraphBuilder = this)
        }
    }

    NavHost(
        navController = navController,
        graph = navGraph
    )

}

private fun companyInfoComposable(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
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

private fun companyListingComposable(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) = with(navGraphBuilder) {
    composable<CompanyListing> {
        val lifecycle = LocalLifecycleOwner.current
        val context = LocalContext.current

        val companyListingViewModel: CompanyListingViewModel = hiltViewModel()
        val snackbarHostState = remember { SnackbarHostState() }

        with(companyListingViewModel) {
            val companyListingState by state.collectAsStateWithLifecycle()

            // TODO: extract to separate block
            LaunchedEffect(lifecycle) {
                lifecycle.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    event.distinctUntilChanged()
                        .collectLatest { event ->
                            when (event) {
                                is ViewModelListingEvents.NavigateToCompanyInfo -> {
                                    navController.navigate(CompanyInfo(event.name))
                                }

                                is ViewModelEvents.DatabaseError -> {
                                    snackbarHostState.run {
                                        currentSnackbarData?.dismiss()
                                        showSnackbar(
                                            getString(
                                                context,
                                                R.string.there_s_no_cached_data_check_your_internet_connection_or_try_later
                                            ),
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Indefinite
                                        )
                                    }
                                }

                                is ViewModelEvents.NetworkError -> {
                                    snackbarHostState.run {
                                        currentSnackbarData?.dismiss()
                                        showSnackbar(
                                            getString(
                                                context,
                                                R.string.there_s_internet_connection_problem_check_your_internet_connection_or_try_later
                                            ),
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Indefinite
                                        )
                                    }
                                }

                                is ViewModelListingEvents.DismissSnackbar -> {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                }
                            }
                        }
                }
            }

            CompanyListingScreen(
                companyListingState = companyListingState,
                snackbarHostState = snackbarHostState,
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
}
