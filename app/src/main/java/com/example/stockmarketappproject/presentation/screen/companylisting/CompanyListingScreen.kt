@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.stockmarketappproject.presentation.screen.companylisting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stockmarketappproject.R
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingState
import com.example.stockmarketappproject.presentation.screen.component.CompanyListingItem

@Composable
fun CompanyListingScreen(
    modifier: Modifier = Modifier,
    companyListingState: CompanyListingState,
    snackbarHostState: SnackbarHostState,
    onSearchQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    onNavigateToCompanyInfo: (String) -> Unit,
) = with(companyListingState) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = searchQuery,
                label = {
                    Text(stringResource(R.string.search))
                },
                placeholder = {
                    Text(stringResource(R.string.type_company_name_or_symbol))
                },
                maxLines = 1,
                singleLine = true,
                onValueChange = { query ->
                    onSearchQueryChanged(query)
                },
                enabled = companies.isNotEmpty()
            )
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                isRefreshing = isRefreshing,
                onRefresh = {
                    onRefresh()
                }
            ) {
                if (companies.isEmpty() && !isRefreshing) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(stringResource(R.string.there_s_no_data_to_display_try_again_later_or_refresh))
                        Button(onClick = {
                            onRefresh()
                        }) {
                            Text(stringResource(R.string.refresh))
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(companies) { company ->
                            CompanyListingItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToCompanyInfo(company.symbol)
                                    }
                                    .padding(16.dp),
                                companyListingPresentation = company
                            )
                            // TODO: think about extract this validation to separate function
                            if (company != companies.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}