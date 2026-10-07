package com.example.packguide.ui.screens

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
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.packguide.ui.theme.BackgroundLight
import com.example.packguide.ui.theme.BorderSlate
import com.example.packguide.ui.theme.CardSurfaceLight
import com.example.packguide.ui.theme.PrimaryBlack
import com.example.packguide.ui.theme.SecondarySlate
import com.example.packguide.ui.theme.SurfaceVariantLight
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier
) {
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf("MANUAL") } // [AUTO] vs [MANUAL]

    // Manual measures state
    var altoCm by remember { mutableIntStateOf(55) }
    var anchoCm by remember { mutableIntStateOf(40) }
    var profCm by remember { mutableIntStateOf(20) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // --- 1. HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundLight)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Back action */ },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = PrimaryBlack
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "SYS // PACKING_OS",
                style = Typography.titleMedium,
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlack,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { isDarkMode = !isDarkMode },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                    contentDescription = "Modo de luz/oscuridad",
                    tint = PrimaryBlack
                )
            }

            IconButton(
                onClick = { /* Settings action */ },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Configuración",
                    tint = PrimaryBlack
                )
            }
        }

        DashedDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // --- 2. SUBHEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(SurfaceVariantLight, shape = RoundedCornerShape(2.dp))
                .border(1.dp, BorderSlate, shape = RoundedCornerShape(2.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ESPACIO MAPEO: 100% // LIDAR ACTIVE | FPS: 60",
                    style = Typography.labelSmall.copy(fontSize = 11.sp),
                    fontFamily = TechMonospaceFontFamily,
                    color = PrimaryBlack
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
                        .background(PrimaryBlack, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, BorderSlate, shape = RoundedCornerShape(4.dp))
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
                            text = "TARGET_LOCK: CARRY_ON",
                            containerColor = PrimaryBlack.copy(alpha = 0.85f),
                            contentColor = AccentRedLed,
                            borderColor = AccentRedLed
                        )

                        TechBadge(
                            text = "IATA VALID",
                            containerColor = PrimaryBlack.copy(alpha = 0.85f),
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
                            .background(CardSurfaceLight, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ANCHO: $anchoCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlack
                        )
                    }

                    // Floating Dimension Callout: ALTO: 55 CM (Left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 10.dp)
                            .background(CardSurfaceLight, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ALTO: $altoCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlack
                        )
                    }

                    // Floating Dimension Callout: PROF: 20 CM (Right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .background(CardSurfaceLight, shape = RoundedCornerShape(2.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PROF: $profCm CM",
                            style = Typography.labelSmall.copy(fontSize = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlack
                        )
                    }

                    // Central Floating Badge: 38L VOLUMEN INTERNO
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(4.dp))
                            .background(CardSurfaceLight, shape = RoundedCornerShape(4.dp))
                            .border(1.5.dp, PrimaryBlack, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "38L VOLUMEN INTERNO",
                                style = Typography.titleMedium,
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlack
                            )
                        }
                    }
                }
            }

            // --- 4. MODE SWITCHER: [AUTO] vs [MANUAL] ---
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceVariantLight, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, BorderSlate, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selectedMode == "AUTO") PrimaryBlack else Color.Transparent,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .clickable { selectedMode = "AUTO" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[AUTO]",
                            style = Typography.labelLarge,
                            fontFamily = TechMonospaceFontFamily,
                            color = if (selectedMode == "AUTO") CardSurfaceLight else PrimaryBlack
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selectedMode == "MANUAL") PrimaryBlack else Color.Transparent,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .clickable { selectedMode = "MANUAL" }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (selectedMode == "MANUAL") {
                                LedIndicator(color = AccentRedLed, size = 5.dp, isPulsing = true)
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = "[MANUAL]",
                                style = Typography.labelLarge,
                                fontFamily = TechMonospaceFontFamily,
                                color = if (selectedMode == "MANUAL") CardSurfaceLight else PrimaryBlack
                            )
                        }
                    }
                }
            }

            // --- 5. MANUAL ADJUSTMENT MODAL CARD ---
            if (selectedMode == "MANUAL") {
                item {
                    TechCard(
                        headerTag = "AJUSTE MANUAL DE MEDIDAS",
                        subTag = "[ CARRY-ON SPEC ]",
                        borderColor = PrimaryBlack
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Dimension Stepper for ALTO
                            DimensionStepperRow(
                                label = "ALTO",
                                valueCm = altoCm,
                                onValueChange = { altoCm = it }
                            )

                            // Dimension Stepper for ANCHO
                            DimensionStepperRow(
                                label = "ANCHO",
                                valueCm = anchoCm,
                                onValueChange = { anchoCm = it }
                            )

                            // Dimension Stepper for PROF.
                            DimensionStepperRow(
                                label = "PROF.",
                                valueCm = profCm,
                                onValueChange = { profCm = it }
                            )

                            DashedDivider(color = BorderSlate)

                            // Calculated Volume Display: "VOLUMEN CALCULADO: 44.0L / 44.000 CM³"
                            val volCm3 = altoCm * anchoCm * profCm
                            val volL = volCm3 / 1000.0
                            val volCm3Formatted = String.format(Locale.US, "%,d", volCm3).replace(',', '.')
                            val volLFormatted = String.format(Locale.US, "%.1f", volL)
                            val isIataOk = altoCm <= 55 && anchoCm <= 40 && profCm <= 23

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceVariantLight, shape = RoundedCornerShape(2.dp))
                                    .border(1.dp, BorderSlate, shape = RoundedCornerShape(2.dp))
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
                                            color = SecondarySlate
                                        )
                                        TechBadge(
                                            text = if (isIataOk) "CABINA IATA OK" else "EXCEDE CABINA",
                                            containerColor = if (isIataOk) PrimaryBlack else AccentRedLed,
                                            contentColor = CardSurfaceLight,
                                            ledColor = if (isIataOk) AccentGreenLed else AccentRedLed
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "${volLFormatted}L / $volCm3Formatted CM³",
                                        style = Typography.headlineMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = PrimaryBlack
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Outline button [ CANCELAR ]
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(CardSurfaceLight, shape = RoundedCornerShape(20.dp))
                                        .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(20.dp))
                                        .clickable {
                                            altoCm = 55
                                            anchoCm = 40
                                            profCm = 20
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "[ CANCELAR ]",
                                        style = Typography.titleMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = PrimaryBlack
                                    )
                                }

                                // Dark pill button [ APLICAR MEDIDAS ]
                                Box(
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .background(PrimaryBlack, shape = RoundedCornerShape(20.dp))
                                        .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(20.dp))
                                        .clickable { /* Apply measures */ }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "[ APLICAR MEDIDAS ]",
                                        style = Typography.titleMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = CardSurfaceLight
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- 6. BOTTOM ACTION BUTTON ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, shape = RoundedCornerShape(28.dp))
                        .background(PrimaryBlack, shape = RoundedCornerShape(28.dp))
                        .border(1.dp, AccentRedLed, shape = RoundedCornerShape(28.dp))
                        .clickable { /* Optimize action */ }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
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
                            text = "OPTIMIZAR - PULSA PARA GENERAR DISTRIBUCIÓN",
                            style = Typography.labelLarge.copy(fontSize = 11.sp),
                            fontFamily = TechMonospaceFontFamily,
                            color = CardSurfaceLight
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
            color = PrimaryBlack,
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
                    .background(SurfaceVariantLight, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, BorderSlate, shape = RoundedCornerShape(4.dp))
                    .clickable { if (valueCm > 1) onValueChange(valueCm - 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    style = Typography.titleLarge,
                    fontFamily = TechMonospaceFontFamily,
                    color = PrimaryBlack
                )
            }

            // Value display Box
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(36.dp)
                    .background(CardSurfaceLight, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$valueCm CM",
                    style = Typography.titleMedium,
                    fontFamily = TechMonospaceFontFamily,
                    color = PrimaryBlack
                )
            }

            // Increment button (+)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PrimaryBlack, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(4.dp))
                    .clickable { onValueChange(valueCm + 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = Typography.titleLarge,
                    fontFamily = TechMonospaceFontFamily,
                    color = CardSurfaceLight
                )
            }
        }
    }
}
