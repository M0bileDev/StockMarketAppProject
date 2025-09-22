@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.stockmarketappproject.presentation.screen.companylisting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stockmarketappproject.R
import com.example.stockmarketappproject.presentation.model.CompanyListingEvent
import com.example.stockmarketappproject.presentation.screen.component.CompanyListingItem

@Composable
fun CompanyListingScreen(
    modifier: Modifier = Modifier,
    viewModel: CompanyListingViewModel = hiltViewModel<CompanyListingViewModel>()
) = with(viewModel) {

    val companyListingState by state.collectAsStateWithLifecycle()

    with(companyListingState) {
        Column(modifier = modifier.fillMaxSize()) {
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
                    onEvent(CompanyListingEvent.OnSearchQueryChange(query))
                }
            )
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                isRefreshing = isRefreshing,
                onRefresh = {
                    onEvent(CompanyListingEvent.OnRefresh)
                }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(companies) { company ->
                        CompanyListingItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // TODO: add logic
                                }
                                .padding(16.dp),
                            companyListingPresentation = company
                        )
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