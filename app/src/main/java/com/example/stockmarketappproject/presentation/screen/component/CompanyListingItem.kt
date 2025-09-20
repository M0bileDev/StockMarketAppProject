package com.example.stockmarketappproject.presentation.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketappproject.R
import com.example.stockmarketappproject.presentation.model.CompanyListingPresentation
import com.example.stockmarketappproject.presentation.ui.theme.StockMarketAppProjectTheme

@Composable
fun CompanyListingItem(
    modifier: Modifier = Modifier,
    companyListingPresentation: CompanyListingPresentation
) = with(companyListingPresentation) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = exchange,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.symbol_x, symbol),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun CompanyListingItemPreview() {
    StockMarketAppProjectTheme {
        CompanyListingItem(
            companyListingPresentation = CompanyListingPresentation(
                "COMPANY NAME COMPANY NAME COMPANY NAME ",
                "SYMBOL",
                "EXCHANGE"
            )
        )
    }
}