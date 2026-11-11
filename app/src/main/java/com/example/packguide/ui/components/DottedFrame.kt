package com.example.packguide.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import kotlin.math.ceil

/** Evenly spaced circular dots following a rounded frame; cached per size. */
fun Modifier.dottedFrame(color: Color, glow: Boolean = false, cornerRadius: Dp = 23.dp): Modifier = drawWithCache {
    val inset = 3.dp.toPx()
    val path = Path().apply {
        addRoundRect(RoundRect(Rect(inset, inset, (size.width - inset).coerceAtLeast(inset),
            (size.height - inset).coerceAtLeast(inset)), CornerRadius(cornerRadius.toPx())))
    }
    val measure = PathMeasure().apply { setPath(path, false) }
    val count = ceil(measure.length / 7.dp.toPx()).toInt().coerceAtLeast(1)
    val points = List(count) { measure.getPosition(it * measure.length / count) }
    onDrawWithContent {
        drawContent()
        points.forEach { point ->
            if (glow) drawCircle(color.copy(alpha = color.alpha * .12f), 3.dp.toPx(), point)
            drawCircle(color, 1.dp.toPx(), point)
        }
    }
}
