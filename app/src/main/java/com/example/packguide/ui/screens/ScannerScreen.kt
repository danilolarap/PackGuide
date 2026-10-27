package com.example.packguide.ui.screens

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.LocalAppLanguageimport com.example.packguide.ui.localization.localizedTextimport androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.example.packguide.ui.components.DashedDivider
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.components.TechBadge
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    packing: PackingState,    onOpenGuide: () -> Unit) {

    // Manual measures state
    val altoCm = packing.heightCm.value    val anchoCm = packing.widthCm.value    val profCm = packing.depthCm.value
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PackGuideColors.background)
    ) {
        // --- 1. HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PackGuideColors.background)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SYS // PACKING_OS",
                style = Typography.titleMedium,
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = PackGuideColors.primaryText,
                modifier = Modifier.weight(1f)
            )

        }

        DashedDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // --- 2. SUBHEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(2.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AJUSTA LAS MEDIDAS DE TU EQUIPAJE",                    style = Typography.labelSmall.copy(fontSize = 11.sp),
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.primaryText
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // --- 3. AR CAMERA VIEWPORT ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(Color(0xFF0F0F11), shape = RoundedCornerShape(4.dp))                        .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(4.dp))
                ) {
                    // Compose Canvas drawing simulated open suitcase & red bounding box overlays & AR crosshairs
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        // Grid pattern
                        val gridStep = 30f
                        for (x in 0..width.toInt() step gridStep.toInt()) {
                            for (y in 0..height.toInt() step gridStep.toInt()) {
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.08f),
                                    radius = 1f,
                                    center = Offset(x.toFloat(), y.toFloat())
                                )
                            }
                        }

                        // Open Suitcase 3D wireframe / bounding box coordinates (#FF2A2A)
                        val boxLeft = width * 0.22f
                        val boxTop = height * 0.20f
                        val boxRight = width * 0.78f
                        val boxBottom = height * 0.78f
                        val depthOffset = 30f

                        // Back face of open suitcase
                        val backLeft = boxLeft + depthOffset
                        val backTop = boxTop - depthOffset * 0.6f
                        val backRight = boxRight + depthOffset
                        val backBottom = boxBottom - depthOffset * 0.6f

                        val strokeStyle = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )
                        val solidStroke = Stroke(width = 2.5f)

                        // Draw back wireframe
                        drawRect(
                            color = AccentRedLed.copy(alpha = 0.4f),
                            topLeft = Offset(backLeft, backTop),
                            size = Size(backRight - backLeft, backBottom - backTop),
                            style = strokeStyle
                        )

                        // Connecting 3D perspective depth lines
                        drawLine(AccentRedLed.copy(alpha = 0.5f), Offset(boxLeft, boxTop), Offset(backLeft, backTop), strokeWidth = 1.5f)
                        drawLine(AccentRedLed.copy(alpha = 0.5f), Offset(boxRight, boxTop), Offset(backRight, backTop), strokeWidth = 1.5f)
                        drawLine(AccentRedLed.copy(alpha = 0.5f), Offset(boxLeft, boxBottom), Offset(backLeft, backBottom), strokeWidth = 1.5f)
                        drawLine(AccentRedLed.copy(alpha = 0.5f), Offset(boxRight, boxBottom), Offset(backRight, backBottom), strokeWidth = 1.5f)

                        // Suitcase inner compartment divider lines
                        drawLine(
                            color = AccentRedLed.copy(alpha = 0.6f),
                            start = Offset((boxLeft + boxRight) / 2f, boxTop),
                            end = Offset((boxLeft + boxRight) / 2f, boxBottom),
                            strokeWidth = 1.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)
                        )

                        // Front Bounding Box (#FF2A2A)
                        drawRect(
                            color = AccentRedLed,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxRight - boxLeft, boxBottom - boxTop),
                            style = solidStroke
                        )

                        // Corner Reticle Brackets
                        val bracketLen = 20f
                        val bracketStroke = 4f
                        // Top-Left
                        drawLine(AccentRedLed, Offset(boxLeft, boxTop), Offset(boxLeft + bracketLen, boxTop), strokeWidth = bracketStroke)
                        drawLine(AccentRedLed, Offset(boxLeft, boxTop), Offset(boxLeft, boxTop + bracketLen), strokeWidth = bracketStroke)
                        // Top-Right
                        drawLine(AccentRedLed, Offset(boxRight, boxTop), Offset(boxRight - bracketLen, boxTop), strokeWidth = bracketStroke)
                        drawLine(AccentRedLed, Offset(boxRight, boxTop), Offset(boxRight, boxTop + bracketLen), strokeWidth = bracketStroke)
                        // Bottom-Left
                        drawLine(AccentRedLed, Offset(boxLeft, boxBottom), Offset(boxLeft + bracketLen, boxBottom), strokeWidth = bracketStroke)
                        drawLine(AccentRedLed, Offset(boxLeft, boxBottom), Offset(boxLeft, boxBottom - bracketLen), strokeWidth = bracketStroke)
                        // Bottom-Right
                        drawLine(AccentRedLed, Offset(boxRight, boxBottom), Offset(boxRight - bracketLen, boxBottom), strokeWidth = bracketStroke)
                        drawLine(AccentRedLed, Offset(boxRight, boxBottom), Offset(boxRight, boxBottom - bracketLen), strokeWidth = bracketStroke)

                        // Center AR Crosshair
                        val centerX = width / 2f
                        val centerY = height / 2f
                        drawCircle(
                            color = AccentRedLed,
                            radius = 24f,
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 1.5f)
                        )
                        drawCircle(
                            color = AccentGreenLed,
                            radius = 6f,
                            center = Offset(centerX, centerY)
                        )
                        drawLine(AccentRedLed, Offset(centerX - 36f, centerY), Offset(centerX - 12f, centerY), strokeWidth = 1.5f)
                        drawLine(AccentRedLed, Offset(centerX + 12f, centerY), Offset(centerX + 36f, centerY), strokeWidth = 1.5f)
                        drawLine(AccentRedLed, Offset(centerX, centerY - 36f), Offset(centerX, centerY - 12f), strokeWidth = 1.5f)
                        drawLine(AccentRedLed, Offset(centerX, centerY + 12f), Offset(centerX, centerY + 36f), strokeWidth = 1.5f)
                    }

                    // Viewport Top Overlaid Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TechBadge(
                            text = "VISTA ILUSTRATIVA",                            containerColor = PackGuideColors.primaryText.copy(alpha = 0.85f),
                            contentColor = AccentRedLed,
                            borderColor = AccentRedLed
                        )

                        TechBadge(
                            text = packing.preset.value.label,                            containerColor = PackGuideColors.primaryText.copy(alpha = 0.85f),
                            contentColor = Color.White,
                            ledColor = AccentGreenLed,
                            borderColor = AccentGreenLed
                        )
                    }

                    // Floating Dimension Callout: ANCHO: 40 CM
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 36.dp)
                            .background(PackGuideColors.surface, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ANCHO: $anchoCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PackGuideColors.primaryText
                        )
                    }

                    // Floating Dimension Callout: ALTO: 55 CM (Left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 10.dp)
                            .background(PackGuideColors.surface, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ALTO: $altoCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PackGuideColors.primaryText
                        )
                    }

                    // Floating Dimension Callout: PROF: 20 CM (Right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .background(PackGuideColors.surface, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PROF: $profCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PackGuideColors.primaryText
                        )
                    }

                    // Central Floating Badge: 38L VOLUMEN INTERNO
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(4.dp))
                            .background(PackGuideColors.surface, shape = RoundedCornerShape(4.dp))
                            .border(1.5.dp, PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${String.format(Locale.US, "%.1f", packing.capacityL)}L VOLUMEN ESTIMADO",                                style = Typography.titleMedium,
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PackGuideColors.primaryText
                            )
                        }
                    }
                }
            }

            item {                Text("TIPO DE EQUIPAJE", style = Typography.titleMedium, color = PackGuideColors.primaryText)                Spacer(Modifier.height(8.dp))                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {                    LuggagePreset.entries.forEach { preset ->                        val selected = packing.preset.value == preset                        Box(                            modifier = Modifier.weight(1f)                                .background(if (selected) PackGuideColors.primaryText else PackGuideColors.surface)                                .border(1.dp, if (selected) AccentRedLed else PackGuideColors.border)                                .clickable { packing.selectPreset(preset) }                                .padding(vertical = 10.dp),                            contentAlignment = Alignment.Center                        ) {                            Text(preset.label, style = Typography.labelSmall,                                color = if (selected) PackGuideColors.surface else PackGuideColors.primaryText)                        }                    }                }                Spacer(Modifier.height(6.dp))                Text("MANO 45×35×20 · CABINA 55×35×25 · BODEGA 75×50×30 cm (ejemplo; verifica tu aerolínea, bodega máx. 158 cm lineales)",                    style = Typography.labelSmall, color = PackGuideColors.secondaryText)            }            // --- 5. MANUAL ADJUSTMENT MODAL CARD ---
            item {
                    TechCard(
                        headerTag = "AJUSTE MANUAL DE MEDIDAS",
                        subTag = "[ CARRY-ON SPEC ]",
                        borderColor = PackGuideColors.primaryText
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Dimension Stepper for ALTO
                            DimensionStepperRow(
                                label = "ALTO",
                                valueCm = altoCm,
                                onValueChange = { packing.heightCm.value = it }                            )

                            // Dimension Stepper for ANCHO
                            DimensionStepperRow(
                                label = "ANCHO",
                                valueCm = anchoCm,
                                onValueChange = { packing.widthCm.value = it }                            )

                            // Dimension Stepper for PROF.
                            DimensionStepperRow(
                                label = "PROF.",
                                valueCm = profCm,
                                onValueChange = { packing.depthCm.value = it }                            )

                            DashedDivider(color = PackGuideColors.border)

                            // Calculated Volume Display: "VOLUMEN CALCULADO: 44.0L / 44.000 CM³"
                            val volCm3 = altoCm * anchoCm * profCm
                            val volL = volCm3 / 1000.0
                            val volCm3Formatted = String.format(Locale.US, "%,d", volCm3).replace(',', '.')
                            val volLFormatted = String.format(Locale.US, "%.1f", volL)
                            val isIataOk = if (packing.preset.value == LuggagePreset.Checked)                                altoCm + anchoCm + profCm <= 158                            else altoCm <= packing.preset.value.heightCm &&                                anchoCm <= packing.preset.value.widthCm &&                                profCm <= packing.preset.value.depthCm
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(2.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "VOLUMEN CALCULADO:",
                                            style = Typography.labelMedium,
                                            fontFamily = TechMonospaceFontFamily,
                                            color = PackGuideColors.secondaryText
                                        )
                                        TechBadge(
                                            text = if (isIataOk) "DENTRO DE REFERENCIA" else "REVISAR MEDIDAS",                                            containerColor = if (isIataOk) PackGuideColors.primaryText else AccentRedLed,
                                            contentColor = PackGuideColors.surface,
                                            ledColor = if (isIataOk) AccentGreenLed else AccentRedLed
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "${volLFormatted}L / $volCm3Formatted CM³",
                                        style = Typography.headlineMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = PackGuideColors.primaryText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(                                text = "Los cambios se reflejan al instante en catálogo y resumen.",                                style = Typography.labelSmall,                                color = PackGuideColors.secondaryText                            )                        }
                    }
                }

            // --- 6. BOTTOM ACTION BUTTON ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, shape = RoundedCornerShape(28.dp))
                        .background(PackGuideColors.primaryText, shape = RoundedCornerShape(28.dp))
                        .border(1.dp, AccentRedLed, shape = RoundedCornerShape(28.dp))
                        .clickable(onClick = onOpenGuide)                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CenterFocusWeak,
                            contentDescription = null,
                            tint = AccentRedLed,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONTINUAR A GUÍA DE EMPACADO",                            style = Typography.labelLarge.copy(fontSize = 11.sp),
                            fontFamily = TechMonospaceFontFamily,
                            color = PackGuideColors.surface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DimensionStepperRow(
    label: String,
    valueCm: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = Typography.titleMedium,
            fontFamily = TechMonospaceFontFamily,
            color = PackGuideColors.primaryText,
            modifier = Modifier.width(80.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Decrement button (-)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(4.dp))
                    .clickable { if (valueCm > 1) onValueChange(valueCm - 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    style = Typography.titleLarge,
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.primaryText
                )
            }

            // Value display Box
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(36.dp)
                    .background(PackGuideColors.surface, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$valueCm CM",
                    style = Typography.titleMedium,
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.primaryText
                )
            }

            // Increment button (+)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
                    .clickable { onValueChange(valueCm + 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = Typography.titleLarge,
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.surface
                )
            }
        }
    }
}
