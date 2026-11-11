package com.example.packguide.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

/** Outside the scrolling inventory: every selected bag's space stays visible. */
@Composable
internal fun CatalogCapacityOverview(packing: PackingState, onCreatePlan: () -> Unit) {
    val copy = PackingText(LocalAppLanguage.current)
    Surface(shape = RoundedCornerShape(20.dp), color = PackGuideColors.surface,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("ESPACIO POR MALETA", style = Typography.labelMedium, color = PackGuideColors.primaryText)
            packing.selectedLuggage.forEach { type ->
                val estimate = packing.bag(type).estimate
                val color = if (estimate.exceedsCapacity) AccentRedLed else PackGuideColors.primaryText
                Column(Modifier.testTag("capacity-${type.name}"), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(type.label, style = Typography.labelMedium, color = PackGuideColors.primaryText,
                            modifier = Modifier.weight(1f))
                        Text(copy.balance(estimate), style = Typography.labelSmall, color = color)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(copy.volume(estimate), style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                        LinearProgressIndicator(progress = { estimate.occupancy.toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier.weight(1f).height(6.dp), color = color,
                            trackColor = PackGuideColors.surfaceVariant, drawStopIndicator = {})
                    }
                }
            }
            AsciiButton("CREAR PLAN DE EMPACADO", onClick = onCreatePlan,
                icon = Icons.AutoMirrored.Outlined.ArrowForward, modifier = Modifier.fillMaxWidth())
        }
    }
}
