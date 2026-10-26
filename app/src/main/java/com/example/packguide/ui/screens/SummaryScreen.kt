package com.example.packguide.ui.screens

import com.example.packguide.ui.theme.PackGuideColors

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.DashedDivider
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.components.TechBadge
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null,
    onOpenSettings: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PackGuideColors.background)
    ) {
        // --- 1. HEADER WITH THEME SWITCH & STATUS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PackGuideColors.background)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SYS // FINAL_DIAGNOSTIC",
                    style = Typography.labelSmall,
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.secondaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "RESUMEN FINAL",
                    style = Typography.headlineMedium,
                    fontFamily = TechMonospaceFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = PackGuideColors.primaryText
                )
            }

            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .size(36.dp)
                    .background(PackGuideColors.surface, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Configuración",
                    tint = PackGuideColors.primaryText,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            LedIndicator(
                color = AccentGreenLed,
                size = 7.dp,
                label = "[ ONLINE ]"
            )
        }

        DashedDivider(modifier = Modifier.padding(horizontal = 16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))

                // --- 2. TOP BLUEPRINT CARD: MAPA DE DISTRIBUCIÓN // 38L - VOL: 38.0 L ---
                TechCard(
                    headerTag = "MAPA DE DISTRIBUCIÓN // 38L - VOL: 38.0 L",
                    subTag = "[ VISTA LATERAL ]",
                    borderColor = PackGuideColors.primaryText
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Visual Schematic of packed luggage showing layered compartments
                        LuggageLayeredSchematicCanvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        DashedDivider(color = PackGuideColors.border)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Legend at bottom of card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TechBadge(
                                text = "[• PESADOS]",
                                containerColor = PackGuideColors.primaryText,
                                contentColor = PackGuideColors.surface,
                                ledColor = AccentRedLed
                            )

                            TechBadge(
                                text = "[• ENROLLADOS]",
                                containerColor = PackGuideColors.primaryText,
                                contentColor = PackGuideColors.surface,
                                ledColor = AccentGreenLed
                            )

                            TechBadge(
                                text = "[• ACCESO RÁPIDO]",
                                containerColor = PackGuideColors.surfaceVariant,
                                contentColor = PackGuideColors.primaryText,
                                borderColor = PackGuideColors.primaryText,
                                ledColor = PackGuideColors.secondaryText
                            )
                        }
                    }
                }
            }

            item {
                // --- 3. AR LIDAR SCAN CONFIRMATION CARD ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(4.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Realtime Top-Down Camera View & LiDAR Scan Overlay
                        LiDarScanCanvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Glowing Green Text Badges
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CAM-01: TOP-DOWN REALTIME",
                                    style = Typography.labelSmall.copy(fontSize = 11.sp),
                                    fontFamily = TechMonospaceFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGreenLed
                                )
                            }

                            Text(
                                text = "LIDAR SURFACE MAPPING",
                                style = Typography.labelSmall,
                                fontFamily = TechMonospaceFontFamily,
                                color = AccentGreenLed.copy(alpha = 0.9f)
                            )

                            Text(
                                text = "BOUNDING BOX: PASS",
                                style = Typography.labelSmall,
                                fontFamily = TechMonospaceFontFamily,
                                color = AccentGreenLed.copy(alpha = 0.9f)
                            )

                            Text(
                                text = "MATCH: 100% OK",
                                style = Typography.labelMedium,
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = AccentGreenLed
                            )
                        }
                    }
                }
            }

            item {
                // --- 4. EFFICIENCY METRICS CARD ---
                TechCard(
                    headerTag = "EFICIENCIA & MÉTRICAS DE EQUIPAJE",
                    borderColor = PackGuideColors.primaryText
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Large Title
                        Text(
                            text = "EFICIENCIA DE VOLUMEN: 92%",
                            style = Typography.headlineMedium,
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PackGuideColors.primaryText
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Filled Progress Bar 92%
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                                .border(1.dp, PackGuideColors.primaryText, shape = RoundedCornerShape(2.dp))
                                .padding(1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .fillMaxSize()
                                    .background(AccentGreenLed, shape = RoundedCornerShape(1.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        DashedDivider(color = PackGuideColors.border)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Stat Box 1 & Stat Box 2 Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Stat Box 1: PESO TOTAL
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(2.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "PESO TOTAL: 9.8 KG",
                                        style = Typography.labelMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = PackGuideColors.primaryText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ESTADO: ÓPTIMO CABINA",
                                        style = Typography.labelSmall.copy(fontSize = 9.sp),
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGreenLed
                                    )
                                }
                            }

                            // Stat Box 2: ESPACIO LIBRE
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(2.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "ESPACIO LIBRE: 8%",
                                        style = Typography.labelMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = PackGuideColors.primaryText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "+3.0L VOL RESIDUAL",
                                        style = Typography.labelSmall.copy(fontSize = 9.sp),
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGreenLed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                // --- 5. PRIMARY ACTION BUTTONS ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Dark Pill Button: [ GUARDAR CONFIGURACIÓN VIAJE ]
                    AsciiButton(
                        text = "[ GUARDAR CONFIGURACIÓN VIAJE ]",
                        onClick = {
                            Toast.makeText(
                                context,
                                "CONFIGURACIÓN GUARDADA // TRIP-ID: #NT-38-04",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true,
                        icon = Icons.Outlined.BookmarkAdded
                    )

                    // Outline Button: [ REORGANIZAR EQUIPAJE ]
                    AsciiButton(
                        text = "[ REORGANIZAR EQUIPAJE ]",
                        onClick = {
                            onNavigateToTab?.invoke(NavTab.Guide)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = false,
                        icon = Icons.Outlined.Refresh
                    )
                }
            }

            item {
                // --- 6. FOOTER ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "••• NOTHING (R) OS // PACK-ID #NT-38-04",
                        style = Typography.labelSmall,
                        fontFamily = TechMonospaceFontFamily,
                        color = PackGuideColors.secondaryText
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Visual schematic Canvas showing layered luggage compartments
@Composable
private fun LuggageLayeredSchematicCanvas(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Background dark technical container
        drawRoundRect(
            color = PackGuideColors.primaryText,
            topLeft = Offset(0f, 0f),
            size = Size(w, h),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Luggage boundary outline
        val pad = 12f
        drawRoundRect(
            color = Color.White.copy(alpha = 0.5f),
            topLeft = Offset(pad, pad),
            size = Size(w - 2f * pad, h - 2f * pad),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = 1.5f)
        )

        // Dimensions technical gauge line
        val gaugeY = pad + 10f
        drawLine(
            color = PackGuideColors.secondaryText,
            start = Offset(pad + 10f, gaugeY),
            end = Offset(w - pad - 10f, gaugeY),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
        )

        val layerHeight = (h - 2f * pad - 20f) / 4f
        val layerW = w - 2f * pad - 20f
        val startX = pad + 10f
        var startY = pad + 20f

        // --- LAYER 1: TECH & CÁMARA (2.1 KG // 4.5L) ---
        drawRoundRect(
            color = PackGuideColors.secondaryText.copy(alpha = 0.35f),
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f)
        )
        drawRoundRect(
            color = PackGuideColors.border,
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = 1f)
        )

        startY += layerHeight

        // --- LAYER 2: CAMISETAS x4 (ENROLLADO TIGHT) ---
        drawRoundRect(
            color = AccentGreenLed.copy(alpha = 0.25f),
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f)
        )
        drawRoundRect(
            color = AccentGreenLed,
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = 1.5f)
        )

        startY += layerHeight

        // --- LAYER 3: PANTALONES x2 ---
        drawRoundRect(
            color = AccentGreenLed.copy(alpha = 0.20f),
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f)
        )
        drawRoundRect(
            color = AccentGreenLed.copy(alpha = 0.8f),
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = 1.5f)
        )

        startY += layerHeight

        // --- LAYER 4: BASE // CALZADO (2.8 KG) ---
        drawRoundRect(
            color = AccentRedLed.copy(alpha = 0.3f),
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f)
        )
        drawRoundRect(
            color = AccentRedLed,
            topLeft = Offset(startX, startY),
            size = Size(layerW, layerHeight - 4f),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = 1.5f)
        )
    }

    // Overlay Composable text inside the schematic
    Column(
        modifier = modifier.padding(horizontal = 22.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Layer 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1. TECH & CÁMARA (2.1 KG // 4.5L)",
                style = Typography.labelSmall.copy(fontSize = 10.sp),
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = PackGuideColors.surface
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "[ACCESO RÁPIDO]",
                style = Typography.labelSmall.copy(fontSize = 8.sp),
                fontFamily = TechMonospaceFontFamily,
                color = PackGuideColors.secondaryText
            )
        }

        // Layer 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2. CAMISETAS x4 (ENROLLADO TIGHT)",
                style = Typography.labelSmall.copy(fontSize = 10.sp),
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = AccentGreenLed
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "[ENROLLADO]",
                style = Typography.labelSmall.copy(fontSize = 8.sp),
                fontFamily = TechMonospaceFontFamily,
                color = AccentGreenLed
            )
        }

        // Layer 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "3. PANTALONES x2",
                style = Typography.labelSmall.copy(fontSize = 10.sp),
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = AccentGreenLed
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "[ENROLLADO]",
                style = Typography.labelSmall.copy(fontSize = 8.sp),
                fontFamily = TechMonospaceFontFamily,
                color = AccentGreenLed
            )
        }

        // Layer 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "4. BASE // CALZADO (2.8 KG)",
                style = Typography.labelSmall.copy(fontSize = 10.sp),
                fontFamily = TechMonospaceFontFamily,
                fontWeight = FontWeight.Bold,
                color = AccentRedLed
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "[PESADO]",
                style = Typography.labelSmall.copy(fontSize = 8.sp),
                fontFamily = TechMonospaceFontFamily,
                color = AccentRedLed
            )
        }
    }
}

// LiDAR AR Surface Scan Viewfinder Canvas
@Composable
private fun LiDarScanCanvas(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "LiDarScan")
    val scanYProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScanProgress"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Camera viewfinder corner brackets ┌ ┐ └ ┘
        val bracketLen = 20f
        val bracketStroke = 2.5f

        // Top Left
        drawLine(AccentGreenLed, Offset(0f, 0f), Offset(bracketLen, 0f), bracketStroke)
        drawLine(AccentGreenLed, Offset(0f, 0f), Offset(0f, bracketLen), bracketStroke)

        // Top Right
        drawLine(AccentGreenLed, Offset(w, 0f), Offset(w - bracketLen, 0f), bracketStroke)
        drawLine(AccentGreenLed, Offset(w, 0f), Offset(w, bracketLen), bracketStroke)

        // Bottom Left
        drawLine(AccentGreenLed, Offset(0f, h), Offset(bracketLen, h), bracketStroke)
        drawLine(AccentGreenLed, Offset(0f, h), Offset(0f, h - bracketLen), bracketStroke)

        // Bottom Right
        drawLine(AccentGreenLed, Offset(w, h), Offset(w - bracketLen, h), bracketStroke)
        drawLine(AccentGreenLed, Offset(w, h), Offset(w, h - bracketLen), bracketStroke)

        // Center reticle crosshair
        val cx = w / 2f
        val cy = h / 2f
        drawCircle(AccentGreenLed.copy(alpha = 0.6f), 12f, Offset(cx, cy), style = Stroke(width = 1f))
        drawLine(AccentGreenLed.copy(alpha = 0.6f), Offset(cx - 20f, cy), Offset(cx + 20f, cy), 1f)
        drawLine(AccentGreenLed.copy(alpha = 0.6f), Offset(cx, cy - 20f), Offset(cx, cy + 20f), 1f)

        // LiDAR animated green laser scanline
        val currentScanY = h * scanYProgress
        drawLine(
            color = AccentGreenLed,
            start = Offset(0f, currentScanY),
            end = Offset(w, currentScanY),
            strokeWidth = 2f
        )

        // Laser scan shadow/glow line
        drawLine(
            color = AccentGreenLed.copy(alpha = 0.3f),
            start = Offset(0f, currentScanY - 6f),
            end = Offset(w, currentScanY - 6f),
            strokeWidth = 6f
        )
    }
}
