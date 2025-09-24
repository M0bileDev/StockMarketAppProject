package com.example.stockmarketappproject.presentation.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StockChart(modifier: Modifier = Modifier) {
    // spacing between chart in x,y axis -> x = (0,100), y = (max, max-100)
    // values from the left (x axis) and hours from the bottom (y axis)
    val spacing = remember(Unit) { 100f }

    // background color below the chart line
    val transparentGraphColor = remember(Unit) { Color.Green.copy(alpha = 0.5f) }

}