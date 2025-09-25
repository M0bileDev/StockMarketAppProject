package com.example.stockmarketappproject.presentation.screen.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketappproject.presentation.model.intraday.CompanyIntradayInfoPresentation
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    stockInfoList: List<CompanyIntradayInfoPresentation>
) {

    val textMeasurer = rememberTextMeasurer()

    // spacing between chart in x,y axis -> x = (0,100), y = (max, max-100)
    // values from the left (x axis) and hours from the bottom (y axis)
    val spacing = remember(Unit) { 100f }

    val graphColor = remember(Unit) { Color.Green }

    // background color below the chart line
    val transparentGraphColor = remember(Unit) { graphColor.copy(alpha = 0.5f) }

    // TODO: these calculation should be move out of the composition to reduce calculation cost

    // roundToInt round to lower number, that's why plus(1) was called
    val upperValue = remember(stockInfoList) {
        stockInfoList.maxOfOrNull { it.close }?.plus(1)?.roundToInt() ?: 0
    }

    // round to lower value anyway
    val lowerValue = remember(stockInfoList) {
        stockInfoList.minOfOrNull { it.close }?.roundToInt() ?: 0
    }

    Canvas(
        modifier = modifier
    ) {
        // space value in pixels about space between each hour's text
        val spacePerHour = (size.width - spacing) / stockInfoList.size

        //every two hour
        val hourStep = 2
        (0 until stockInfoList.size - 1 step hourStep).forEach { i ->
            val intradayInfo = stockInfoList[i]
            val hour = intradayInfo.timestamp.hour

            // for every hour, space from left will grow like
            // spacing + (i * spacePerHour)
            // spacing = 100, spacePerHour = 100
            // <-100 + (0 * 100) = 100   -> 5 (first hour)
            // <-100 + (1 * 100) = 200         -> 7 (second hour)
            // <-100 + (2 * 100) = 300                 -> 9 (third hour) etc.
            // space will grow in linear manner
            val xOffset = spacing + (i * spacePerHour)

            // static value
            val yOffset = size.height - 8

            drawText(
                topLeft = Offset(xOffset, yOffset),
                textLayoutResult = textMeasurer.measure(
                    text = hour.toString(), style = TextStyle.Default.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                )
            )
        }

        val priceStep = 5
        val priceInterpolation = (upperValue - lowerValue) / priceStep
        (0..priceStep).forEach { i ->
            val price = lowerValue + (priceInterpolation * i)

            // similar as above, space will shrink in linear manner
            val yOffset = size.height - spacing - i * size.height / 5
            val xOffset = 32f

            drawText(
                topLeft = Offset(xOffset, yOffset),
                textLayoutResult = textMeasurer.measure(
                    text = price.toString(), style = TextStyle.Default.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                )
            )
        }

        var lastXControlPoint = 0f
        val strokePath = Path().apply {
            val height = size.height

            // logic will be to transform info (value below) to canvas coordinate system based on percentage of stock value
            // values like:
            // 1400 -> 81-100%
            // 1300 -> 61-80%
            // 1200 -> 41-60%   (if the stock value will be 1210 USD it will be placed on this height of the graph)
            // 1100 -> 21-40%
            // 1000 -> 0-20%
            stockInfoList.forEachIndexed { index, currentIntradayInfo ->

                val nextIndex = index + 1
                val nextIntradayInfo = stockInfoList.getOrNull(nextIndex) ?: stockInfoList.last()

                val ratioDenominator = upperValue - lowerValue
                val startRatio =
                    (currentIntradayInfo.close - lowerValue) / ratioDenominator
                val endRatio = (nextIntradayInfo.close - lowerValue) / ratioDenominator

                val x1 = spacing + index * spacePerHour
                // y1 explanation (each 'y' in any case)
                //
                // - remember we start from point (0,0)
                //
                //  ^ - height
                //  |
                //  |
                //  |                                               ^ - height - spacing - (startRatio * height)
                //  |                                               |
                //  |   ^ - spacing                                 |
                //  |   |               ^ - startRatio * height     |
                //  |   |               |                           |
                //  v   v               v                           v
                val y1 = height - spacing - (startRatio * height).toFloat()

                val x2 = spacing + nextIndex * spacePerHour
                val y2 = height - spacing - (endRatio * height).toFloat()

                if (index == 0) {
                    moveTo(
                        x1, y1
                    )
                }


                // control point which decides in which direction the curve will go to
                // (smooth curve between two coordinates)
                val xControlPoint = (x1 + x2) / 2f

                // TODO: think how can make it better
                lastXControlPoint = xControlPoint
                val yControlPoint = (y1 + y2) / 2f
                // draw smooth lines
                quadraticTo(x1, y1, xControlPoint, yControlPoint)
            }
        }

        // draw stock lines
        drawPath(
            strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        // background under the stock line (will be filled with gradient)
        val fillPath = strokePath.copy().apply {
            lineTo(lastXControlPoint, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
    }


}