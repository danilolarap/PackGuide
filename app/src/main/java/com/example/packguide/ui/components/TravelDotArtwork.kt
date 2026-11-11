package com.example.packguide.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class TravelGlyph(val pixels: List<String>) {
    Plane(listOf(
        ".......#.......", "......###......", "......###......", "......###......",
        ".....#####.....", "....#######....", "...#########...", ".#############.",
        "######...######", ".....#####.....", "......###......", "......###......",
        ".....#####.....", "....#######....", "......###......")),
    Cabin(listOf(
        ".....#####.....", ".....#...#.....", ".....#...#.....", "...#########...",
        "..##.......##..", "..#..#.#.#..#..", "..#..#.#.#..#..", "..#..#.#.#..#..",
        "..#..#.#.#..#..", "..#..#.#.#..#..", "..#..#.#.#..#..", "..##.......##..",
        "...#########...", "....##...##....", "....##...##....")),
    Backpack(listOf(
        ".....#####.....", "....##...##....", "...#########...", "..##.......##..",
        "..#.........#..", ".##..#####..##.", ".##.........##.", ".##.#######.##.",
        ".##.#.....#.##.", ".##.#..#..#.##.", ".##.#.....#.##.", "..#.#######.#..",
        "..##.......##..", "...#########...", "...............")),
    Hold(listOf(
        ".....#####.....", ".....#...#.....", "..###########..", ".##.........##.",
        ".#..#.....#..#.", ".#..#.....#..#.", ".#############.", ".#..#.....#..#.",
        ".#..#.....#..#.", ".#..#.....#..#.", ".##.........##.", "..###########..",
        "...##.....##...", "...##.....##...", "...............")),
    Passport(listOf(
        "...#########...", "..##.......#...", "..#........#...", "..#..#####.#...",
        "..#.#..#..##...", "..#.#.###.##...", "..#.#..#..##...", "..#..#####.#...",
        "..#........#...", "..#.######.#...", "..#........#...", "..#..####..#...",
        "..#........#...", "..##########...", "...............")),
    Globe(listOf(
        ".....#####.....", "...###.#.###...", "..##.#.#.#.##..", ".##..#.#.#..##.",
        ".#.#########.#.", "##..#..#..#..##", "#...#..#..#...#", "###############",
        "#...#..#..#...#", "##..#..#..#..##", ".#.#########.#.", ".##..#.#.#..##.",
        "..##.#.#.#.##..", "...###.#.###...", ".....#####....."))
}

private fun DrawScope.led(point: Offset, radius: Float, color: Color, glow: Boolean) {
    if (glow) {
        drawCircle(color.copy(alpha = color.alpha * 0.06f), radius * 3.2f, point)
        drawCircle(color.copy(alpha = color.alpha * 0.13f), radius * 2f, point)
    }
    drawCircle(color, radius, point)
}

private fun DrawScope.drawTravelGlyph(glyph: TravelGlyph, origin: Offset, width: Float, color: Color, glow: Boolean) {
    val step = width / 15f
    glyph.pixels.forEachIndexed { row, line ->
        line.forEachIndexed { column, pixel ->
            if (pixel == '#') led(origin + Offset((column + .5f) * step, (row + .5f) * step), step * .25f, color, glow)
        }
    }
}

/** Decorative only: no semantics, touch handlers, bitmaps, or animation loops. */
@Composable
fun Modifier.travelDotBackground(variant: Int = 0): Modifier {
    val dark = PackGuideColors.background.luminance() < .3f
    val background = if (dark) Color(0xFF101110) else Color(0xFFF6F6F6)
    val foreground = if (dark) Color.White else Color.Black
    return drawWithCache {
        val unit = 1.dp.toPx()
        val canvasSize = size
        // Cache the dotted contour coordinates until size or theme changes.
        val contours = buildList {
            for (ring in 0..8) {
                val radius = (80 + ring * 16) * unit
                for (sample in 0 until 130) {
                    val angle = sample / 130f * (2 * PI).toFloat()
                    val wave = 1f + .12f * sin(angle * 3 + variant) + .07f * cos(angle * 5)
                    val x = canvasSize.width * 1.04f + cos(angle) * radius * wave
                    val y = canvasSize.height * .47f + sin(angle) * radius * wave * 1.15f
                    if (x in 0f..canvasSize.width && y in 0f..canvasSize.height) add(Offset(x, y))
                }
            }
        }
        onDrawBehind {
            drawRect(background)
            contours.forEach { led(it, .7f * unit, foreground.copy(alpha = if (dark) .12f else .14f), dark) }
            val marks = listOf(
                Triple(TravelGlyph.Plane, Offset(size.width - 96 * unit, 24 * unit), 76f),
                Triple(TravelGlyph.Backpack, Offset(-14 * unit, size.height * .29f), 90f),
                Triple(TravelGlyph.Cabin, Offset(size.width - 72 * unit, size.height * .68f), 88f),
                Triple(TravelGlyph.Globe, Offset(12 * unit, size.height - 114 * unit), 86f),
                Triple(TravelGlyph.Passport, Offset(size.width * .43f, size.height * .89f), 66f)
            )
            marks.forEachIndexed { index, (symbol, origin, width) ->
                val selected = if (variant % 2 == 1 && index == 2) TravelGlyph.Hold else symbol
                drawTravelGlyph(selected, origin, width * unit,
                    foreground.copy(alpha = if (dark) .16f else if (variant == 0) .15f else .24f), dark)
            }
        }
    }
}

@Composable
fun TravelDotIcon(glyph: TravelGlyph, modifier: Modifier = Modifier, accent: Boolean = false) {
    val dark = PackGuideColors.background.luminance() < .3f
    val color = if (accent) AccentRedLed else if (dark) Color.White else Color.Black
    Canvas(modifier.size(48.dp)) {
        drawTravelGlyph(glyph, Offset.Zero, minOf(size.width, size.height), color, dark)
    }
}
