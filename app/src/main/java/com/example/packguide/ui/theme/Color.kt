package com.example.packguide.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Nothing OS / Tech ASCII Palette
val PrimaryBlack = Color(0xFF0F0F11)
val BackgroundLight = Color(0xFFF2F4F7)
val CardSurfaceLight = Color(0xFFFFFFFF)
val AccentRedLed = Color(0xFFFF2A2A)
val AccentGreenLed = Color(0xFF00E676)
val SecondarySlate = Color(0xFF8A8D93)
val BorderSlate = Color(0xFFD1D5DB)
val SurfaceVariantLight = Color(0xFFE5E8EC)

@Immutable
data class TechExtendedColors(
    val primaryBlack: Color = PrimaryBlack,
    val backgroundLight: Color = BackgroundLight,
    val cardSurfaceLight: Color = CardSurfaceLight,
    val accentRedLed: Color = AccentRedLed,
    val accentGreenLed: Color = AccentGreenLed,
    val secondarySlate: Color = SecondarySlate,
    val borderSlate: Color = BorderSlate,
    val surfaceVariantLight: Color = SurfaceVariantLight
)

val LocalTechExtendedColors = staticCompositionLocalOf { TechExtendedColors() }
