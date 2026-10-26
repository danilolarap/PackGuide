package com.example.packguide.ui.screens

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun GuideScreen(
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null
) {
    var packedCount by remember { mutableIntStateOf(3) }
    var activePhase by remember { mutableIntStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PackGuideColors.background)
    ) {
        // --- 1. HEADER WITH STEP TRACKING & PROGRESS BAR ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PackGuideColors.background)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NOTHING // VALIJA AR - PASO 3: DOBLAR CAMISETAS",
                    style = Typography.labelSmall.copy(fontSize = 11.sp),
                    fontFamily = TechMonospaceFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = PackGuideColors.primaryText,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                LedIndicator(
                    color = AccentGreenLed,
                    size = 6.dp,
                    label = "[ PASO 03 / 05 ]"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Step Visual Progress Bar (Step 3 of 5 = 60%)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Paso 03 / 05",
                    style = Typography.labelSmall,
                    fontFamily = TechMonospaceFontFamily,
                    color = PackGuideColors.secondaryText
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Bar container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(PackGuideColors.surfaceVariant, shape = RoundedCornerShape(2.dp))
                        .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(2.dp))
                        .padding(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.60f) // 3 out of 5 steps = 60%
                            .fillMaxSize()
                            .background(AccentGreenLed, shape = RoundedCornerShape(1.dp))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "60%",
                    style = Typography.labelSmall,
                    fontFamily = TechMonospaceFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreenLed
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            DashedDivider()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))

                // --- 2. TOP INSTRUCTION CARD: MÉTODO: RANGER ROLL ---
                TechCard(
                    borderColor = PackGuideColors.primaryText
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Title + Efficiency Badge Header Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MÉTODO: RANGER ROLL",
                                style = Typography.headlineMedium,
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = PackGuideColors.primaryText
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            // Efficiency Badge (-35% VOL)
                            TechBadge(
                                text = "-35% VOL",
                                containerColor = AccentGreenLed,
                                contentColor = PackGuideColors.primaryText,
                                borderColor = PackGuideColors.primaryText,
                                ledColor = PackGuideColors.primaryText
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        DashedDivider(color = PackGuideColors.border)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Illustrated 3-Step Diagram Card
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // FASE 01
                            PhaseStepItem(
                                phaseNumber = "FASE 01",
                                title = "BASE Y DOBLADILLO",
                                description = "Extiende la prenda plana y pliega el borde inferior (8 cm).",
                                isActive = activePhase == 1,
                                onClick = { activePhase = 1 }
                            ) {
                                Phase1DiagramCanvas()
                            }

                            // FASE 02
                            PhaseStepItem(
                                phaseNumber = "FASE 02",
                                title = "PLIEGUE AL CENTRO",
                                description = "Dobla ambos laterales y mangas hacia el eje central.",
                                isActive = activePhase == 2,
                                onClick = { activePhase = 2 }
                            ) {
                                Phase2DiagramCanvas()
                            }

                            // FASE 03
                            PhaseStepItem(
                                phaseNumber = "FASE 03",
                                title = "ENROLLADO Y BLOQUEO",
                                description = "Enrolla con tensión máxima desde el cuello.",
                                isActive = activePhase == 3,
                                onClick = { activePhase = 3 }
                            ) {
                                Phase3DiagramCanvas()
                            }
                        }
                    }
                }
            }

            item {
                // --- 3. SUITCASE 2D LAYER ALLOCATION GRID CARD ---
                TechCard(
                    headerTag = "ASIGNACIÓN EN VALIJA - GRID 2D // CARGA",
                    subTag = "[ VISTA SUPERIOR // CAPA B ]",
                    borderColor = PackGuideColors.primaryText
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Grid View Blueprint Canvas
                        Suitcase2DGridCanvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        DashedDivider(color = PackGuideColors.border)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Metrics row below grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // CAPACIDAD
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "CAPACIDAD",
                                    style = Typography.labelSmall,
                                    color = PackGuideColors.secondaryText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "4.2 L",
                                    style = Typography.titleLarge,
                                    fontFamily = TechMonospaceFontFamily,
                                    color = PackGuideColors.primaryText
                                )
                            }

                            // ESTABILIDAD
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "ESTABILIDAD",
                                    style = Typography.labelSmall,
                                    color = PackGuideColors.secondaryText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "ÓPTIMA",
                                    style = Typography.titleLarge,
                                    fontFamily = TechMonospaceFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGreenLed
                                )
                            }

                            // DISTRIBUCIÓN
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "DISTRIBUCIÓN",
                                    style = Typography.labelSmall,
                                    color = PackGuideColors.secondaryText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "BASE INF",
                                    style = Typography.titleLarge,
                                    fontFamily = TechMonospaceFontFamily,
                                    color = PackGuideColors.primaryText
                                )
                            }
                        }
                    }
                }
            }

            item {
                // --- 4. NAVIGATION ACTION CONTROLS ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Black Pill Button: [ MARCAR EMPACADO (3/6) ]
                    AsciiButton(
                        text = "[ MARCAR EMPACADO ($packedCount/6) ]",
                        onClick = {
                            if (packedCount < 6) packedCount++ else packedCount = 3
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true,
                        isAccentRed = false
                    )

                    // Bottom Row Buttons: Outline buttons "<- ANTERIOR" and "SIGUIENTE ->"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsciiButton(
                            text = "<- ANTERIOR",
                            onClick = {
                                onNavigateToTab?.invoke(NavTab.Catalog)
                            },
                            modifier = Modifier.weight(1f),
                            isPrimary = false,
                            icon = Icons.AutoMirrored.Outlined.ArrowBack
                        )

                        AsciiButton(
                            text = "SIGUIENTE ->",
                            onClick = {
                                onNavigateToTab?.invoke(NavTab.Summary)
                            },
                            modifier = Modifier.weight(1f),
                            isPrimary = false,
                            icon = Icons.AutoMirrored.Outlined.ArrowForward
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PhaseStepItem(
    phaseNumber: String,
    title: String,
    description: String,
    isActive: Boolean,
    onClick: () -> Unit,
    diagramContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isActive) PackGuideColors.surfaceVariant else PackGuideColors.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = if (isActive) AccentRedLed else PackGuideColors.border,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Red Dot Phase Indicator + Diagram Thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, if (isActive) AccentRedLed else PackGuideColors.border, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                diagramContent()

                // Red dot phase number badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(3.dp)
                        .background(AccentRedLed, shape = RoundedCornerShape(2.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = phaseNumber.replace("FASE ", "F"),
                        style = Typography.labelSmall.copy(fontSize = 8.sp),
                        fontFamily = TechMonospaceFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PackGuideColors.surface
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = phaseNumber,
                        style = Typography.labelSmall,
                        fontFamily = TechMonospaceFontFamily,
                        color = AccentRedLed,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "// $title",
                        style = Typography.titleMedium,
                        fontFamily = TechMonospaceFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PackGuideColors.primaryText
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = Typography.bodyMedium,
                    color = PackGuideColors.secondaryText
                )
            }
        }
    }
}

// Minimalist vector diagram for Phase 1: Flat T-Shirt with Bottom Fold (8cm)
@Composable
private fun Phase1DiagramCanvas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokePx = 1.5f

        // Outline flat shirt
        val path = Path().apply {
            moveTo(w * 0.35f, h * 0.2f) // Neck left
            lineTo(w * 0.65f, h * 0.2f) // Neck right
            lineTo(w * 0.85f, h * 0.35f) // Right sleeve outer
            lineTo(w * 0.75f, h * 0.45f) // Right sleeve inner
            lineTo(w * 0.7f, h * 0.38f) // Right armpit
            lineTo(w * 0.7f, h * 0.82f) // Right bottom edge
            lineTo(w * 0.3f, h * 0.82f) // Left bottom edge
            lineTo(w * 0.3f, h * 0.38f) // Left armpit
            lineTo(w * 0.25f, h * 0.45f) // Left sleeve inner
            lineTo(w * 0.15f, h * 0.35f) // Left sleeve outer
            close()
        }

        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.85f),
            style = Stroke(width = strokePx)
        )

        // Folded 8cm bottom edge dashed line in green
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
        drawLine(
            color = AccentGreenLed,
            start = Offset(w * 0.3f, h * 0.72f),
            end = Offset(w * 0.7f, h * 0.72f),
            strokeWidth = 2f,
            pathEffect = dashEffect
        )

        // Fold indicator arrow
        drawLine(
            color = AccentGreenLed,
            start = Offset(w * 0.5f, h * 0.82f),
            end = Offset(w * 0.5f, h * 0.72f),
            strokeWidth = 2f
        )
    }
}

// Minimalist vector diagram for Phase 2: Sides folded into center
@Composable
private fun Phase2DiagramCanvas() {
    val secondaryText = PackGuideColors.secondaryText    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Outer shirt outline ghost
        val pathGhost = Path().apply {
            moveTo(w * 0.35f, h * 0.2f)
            lineTo(w * 0.65f, h * 0.2f)
            lineTo(w * 0.8f, h * 0.35f)
            lineTo(w * 0.7f, h * 0.82f)
            lineTo(w * 0.3f, h * 0.82f)
            lineTo(w * 0.2f, h * 0.35f)
            close()
        }

        drawPath(
            path = pathGhost,
            color = secondaryText.copy(alpha = 0.4f),            style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f))
        )

        // Folded central column
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.38f, h * 0.2f),
            size = Size(w * 0.24f, h * 0.62f),
            cornerRadius = CornerRadius(2f, 2f),
            style = Stroke(width = 1.5f)
        )

        // Center fold line
        drawLine(
            color = AccentRedLed,
            start = Offset(w * 0.5f, h * 0.15f),
            end = Offset(w * 0.5f, h * 0.85f),
            strokeWidth = 1.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
        )

        // Left arrow ->
        drawLine(
            color = AccentRedLed,
            start = Offset(w * 0.22f, h * 0.5f),
            end = Offset(w * 0.35f, h * 0.5f),
            strokeWidth = 1.5f
        )

        // Right arrow <-
        drawLine(
            color = AccentRedLed,
            start = Offset(w * 0.78f, h * 0.5f),
            end = Offset(w * 0.65f, h * 0.5f),
            strokeWidth = 1.5f
        )
    }
}

// Minimalist vector diagram for Phase 3: Tightly rolled shirt cylinder
@Composable
private fun Phase3DiagramCanvas() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Cylinder body
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.25f, h * 0.35f),
            size = Size(w * 0.5f, h * 0.3f),
            cornerRadius = CornerRadius(10f, 10f),
            style = Stroke(width = 1.5f)
        )

        // Spiral roll end cap circles
        drawOval(
            color = AccentGreenLed,
            topLeft = Offset(w * 0.22f, h * 0.35f),
            size = Size(w * 0.12f, h * 0.3f),
            style = Stroke(width = 1.5f)
        )

        drawOval(
            color = AccentGreenLed,
            topLeft = Offset(w * 0.66f, h * 0.35f),
            size = Size(w * 0.12f, h * 0.3f),
            style = Stroke(width = 1.5f)
        )

        // Tight roll horizontal ridges
        for (i in 1..3) {
            val rx = w * (0.32f + i * 0.08f)
            drawLine(
                color = Color.White.copy(alpha = 0.8f),
                start = Offset(rx, h * 0.35f),
                end = Offset(rx, h * 0.65f),
                strokeWidth = 1f
            )
        }

        // Tension locking arrow
        drawLine(
            color = AccentRedLed,
            start = Offset(w * 0.5f, h * 0.2f),
            end = Offset(w * 0.5f, h * 0.32f),
            strokeWidth = 2f
        )
    }
}

// Compose Canvas Blueprint Grid for 2D Suitcase Layer Allocation
@Composable
private fun Suitcase2DGridCanvas(
    modifier: Modifier = Modifier
) {
    val secondaryText = PackGuideColors.secondaryText    val infiniteTransition = rememberInfiniteTransition(label = "TargetPulse")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BorderGlow"
    )

    Box(
        modifier = modifier
            .background(PackGuideColors.primaryText, shape = RoundedCornerShape(4.dp))
            .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle grid lines
            val gridStep = 20f
            val gridPathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 4f), 0f)
            var x = 0f
            while (x < w) {
                drawLine(
                    color = secondaryText.copy(alpha = 0.15f),                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f,
                    pathEffect = gridPathEffect
                )
                x += gridStep
            }
            var y = 0f
            while (y < h) {
                drawLine(
                    color = secondaryText.copy(alpha = 0.15f),                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f,
                    pathEffect = gridPathEffect
                )
                y += gridStep
            }

            // Outer Suitcase Boundary Contour
            drawRoundRect(
                color = Color.White.copy(alpha = 0.4f),
                topLeft = Offset(4f, 4f),
                size = Size(w - 8f, h - 8f),
                cornerRadius = CornerRadius(12f, 12f),
                style = Stroke(width = 1.5f)
            )

            // Inner dividing grid lines (2x2 blueprint slots)
            val midX = w / 2f
            val midY = h / 2f

            val slotDash = PathEffect.dashPathEffect(floatArrayOf(6f, 4f), 0f)

            // Vertical divider
            drawLine(
                color = secondaryText.copy(alpha = 0.4f),                start = Offset(midX, 10f),
                end = Offset(midX, h - 10f),
                strokeWidth = 1f,
                pathEffect = slotDash
            )

            // Horizontal divider
            drawLine(
                color = secondaryText.copy(alpha = 0.4f),                start = Offset(10f, midY),
                end = Offset(w - 10f, midY),
                strokeWidth = 1f,
                pathEffect = slotDash
            )

            // --- SLOT 1: TOP LEFT - [TECH PACK] ---
            drawRoundRect(
                color = secondaryText.copy(alpha = 0.2f),                topLeft = Offset(12f, 12f),
                size = Size(midX - 18f, midY - 18f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // --- SLOT 2: TOP RIGHT - [NECESER] ---
            drawRoundRect(
                color = secondaryText.copy(alpha = 0.2f),                topLeft = Offset(midX + 6f, 12f),
                size = Size(midX - 18f, midY - 18f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // --- SLOT 3: BOTTOM RIGHT - [CALZADO] ---
            drawRoundRect(
                color = secondaryText.copy(alpha = 0.2f),                topLeft = Offset(midX + 6f, midY + 6f),
                size = Size(midX - 18f, midY - 18f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // --- SLOT 4: BOTTOM LEFT - ACTIVE TARGET HIGHLIGHTED WITH GREEN LED BORDER ---
            // Green glow background fill
            drawRoundRect(
                color = AccentGreenLed.copy(alpha = 0.15f * borderGlowAlpha),
                topLeft = Offset(12f, midY + 6f),
                size = Size(midX - 18f, midY - 18f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Green LED border
            drawRoundRect(
                color = AccentGreenLed.copy(alpha = borderGlowAlpha),
                topLeft = Offset(12f, midY + 6f),
                size = Size(midX - 18f, midY - 18f),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 2.5f)
            )

            // Diagonal hatch lines for target zone
            var hatch = 20f
            val hatchEnd = midX - 20f
            while (hatch < hatchEnd) {
                drawLine(
                    color = AccentGreenLed.copy(alpha = 0.25f),
                    start = Offset(hatch, midY + 12f),
                    end = Offset(hatch + 15f, h - 12f),
                    strokeWidth = 1f
                )
                hatch += 15f
            }
        }

        // Overlay Text Badges in 2D Slots
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1f)) {
                // Top Left: [TECH PACK]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "[TECH PACK]",
                        style = Typography.labelSmall,
                        fontFamily = TechMonospaceFontFamily,
                        color = PackGuideColors.surface
                    )
                }

                // Top Right: [NECESER]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "[NECESER]",
                        style = Typography.labelSmall,
                        fontFamily = TechMonospaceFontFamily,
                        color = PackGuideColors.surface
                    )
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                // Bottom Left: Active Target Slot Highlighted with Green LED Border
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LedIndicator(color = AccentGreenLed, size = 6.dp, isPulsing = true)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "[• TARGET ACTIVO x4]",
                                style = Typography.labelSmall.copy(fontSize = 10.sp),
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = AccentGreenLed
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "ZONA INF. IZQUIERDA\n(EQUILIBRIO PESO)",
                            style = Typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 10.sp),
                            fontFamily = TechMonospaceFontFamily,
                            color = AccentGreenLed
                        )
                    }
                }

                // Bottom Right: [CALZADO]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "[CALZADO]",
                        style = Typography.labelSmall,
                        fontFamily = TechMonospaceFontFamily,
                        color = PackGuideColors.surface
                    )
                }
            }
        }
    }
}
