package com.example.stockmarketappproject.presentation.screen.component

import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.example.stockmarketappproject.presentation.model.intraday.CompanyIntradayInfoPresentation
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    stockInfoList: List<CompanyIntradayInfoPresentation>
) {
    // spacing between chart in x,y axis -> x = (0,100), y = (max, max-100)
    // values from the left (x axis) and hours from the bottom (y axis)
    val spacing = remember(Unit) { 100f }

    // background color below the chart line
    val transparentGraphColor = remember(Unit) { Color.Green.copy(alpha = 0.5f) }


    // TODO: these calculation should be move a=out of the composition to reduce calculation cost

    // roundToInt round to lower number, that's why plus(1) was called
    val upperValue = remember(stockInfoList) {
        stockInfoList.maxOfOrNull { it.close }?.plus(1)?.roundToInt() ?: 0
    }

    // round to lower value anyway
    val lowerValue = remember(stockInfoList) {
        stockInfoList.minOfOrNull { it.close }?.roundToInt() ?: 0
    }

    val density = LocalDensity.current
    // draw text on canvas
    val textPainter = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            //coordinate of the text is center not top-left corner
            textAlign = Paint.Align.CENTER
            textSize = with(density) { 12.sp.toPx() }
        }
    }


}