package com.example.stockmarketappproject.framework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.stockmarketappproject.framework.directions.CompanyInfo
import com.example.stockmarketappproject.framework.directions.CompanyListing
import com.example.stockmarketappproject.presentation.screen.companyinfo.CompanyInfoScreen
import com.example.stockmarketappproject.presentation.screen.companylisting.CompanyListingScreen
import com.example.stockmarketappproject.presentation.ui.theme.StockMarketAppProjectTheme
import dagger.hilt.android.AndroidEntryPoint

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
            composable<CompanyListing> {
                CompanyListingScreen(onNavigateToCompanyInfo = { name ->
                    navController.navigate(CompanyInfo(name))
                })
            }
            composable<CompanyInfo> {
                CompanyInfoScreen()
            }
        }
    }

    NavHost(
        navController = navController,
        graph = navGraph
    )

}
