package ru.itis.android.homework9.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

@Composable
fun CircleChart(
    sectorsCount: Int,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
    strokeWidth: Dp = 56.dp,
) {
    require(sectorsCount >= 2) { "sectorsCount must be >= 2" }
    require(colors.size >= sectorsCount) {
        "colors size (${colors.size}) must be >= sectorsCount ($sectorsCount)"
    }
    val palette = colors.take(sectorsCount)
    for (i in palette.indices) {
        val next = (i + 1) % palette.size
        require(palette[i] != palette[next]) {
            "Adjacent sectors have the same color at indices $i and $next"
        }
    }

    var activeIndex by remember(sectorsCount) { mutableIntStateOf(-1) }

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(sectorsCount, strokeWidth) {
                detectTapGestures { tap ->
                    activeIndex = hitTestSector(
                        tap = tap,
                        canvasWidth = this.size.width.toFloat(),
                        canvasHeight = this.size.height.toFloat(),
                        sectorsCount = sectorsCount,
                        strokeWidthPx = strokeWidth.toPx(),
                    )
                }
            }
            .drawBehind {
                drawSectors(
                    sectorsCount = sectorsCount,
                    palette = palette,
                    activeIndex = activeIndex,
                    strokeWidthPx = strokeWidth.toPx(),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = sectorsCount.toString(),
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
            ),
        )
    }
}

private fun DrawScope.drawSectors(
    sectorsCount: Int,
    palette: List<Color>,
    activeIndex: Int,
    strokeWidthPx: Float,
) {
    val canvasMin = min(this.size.width, this.size.height)
    val outerRadius = canvasMin / 2f
    val centerline = outerRadius - strokeWidthPx / 2f
    val capRadius = strokeWidthPx / 2f

    val diameter = 2f * centerline
    val topLeft = Offset(
        x = (this.size.width - diameter) / 2f,
        y = (this.size.height - diameter) / 2f,
    )
    val arcSize = Size(diameter, diameter)
    val cx = this.size.width / 2f
    val cy = this.size.height / 2f

    val sweepPerSector = 360f / sectorsCount
    val baseStart = 180f
    val stroke = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt)

    fun colorOf(i: Int): Color {
        val base = palette[i]
        return if (i == activeIndex) lerp(base, Color.White, 0.4f) else base
    }

    for (i in 0 until sectorsCount) {
        drawArc(
            color = colorOf(i),
            startAngle = baseStart + i * sweepPerSector,
            sweepAngle = sweepPerSector,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = stroke,
        )
    }

    for (i in 0 until sectorsCount) {
        val angleRad = ((baseStart + i * sweepPerSector) * PI / 180.0).toFloat()
        val capCenter = Offset(
            x = cx + centerline * cos(angleRad),
            y = cy + centerline * sin(angleRad),
        )
        drawCircle(color = colorOf(i), radius = capRadius, center = capCenter)
    }
}

private fun hitTestSector(
    tap: Offset,
    canvasWidth: Float,
    canvasHeight: Float,
    sectorsCount: Int,
    strokeWidthPx: Float,
): Int {
    val cx = canvasWidth / 2f
    val cy = canvasHeight / 2f
    val dx = tap.x - cx
    val dy = tap.y - cy
    val distance = hypot(dx, dy)

    val outer = min(canvasWidth, canvasHeight) / 2f
    val inner = outer - strokeWidthPx
    if (distance < inner || distance > outer) return -1

    var degrees = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
    if (degrees < 0f) degrees += 360f
    val relative = ((degrees - 180f) % 360f + 360f) % 360f
    val sweep = 360f / sectorsCount
    return (relative / sweep).toInt().coerceIn(0, sectorsCount - 1)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CircleChartPreview6() {
    CircleChart(
        sectorsCount = 6,
        colors = listOf(
            Color(0xFFE57373),
            Color(0xFF64B5F6),
            Color(0xFFFFD54F),
            Color(0xFF81C784),
            Color(0xFFBA68C8),
            Color(0xFFFFB74D),
        ),
    )
}
