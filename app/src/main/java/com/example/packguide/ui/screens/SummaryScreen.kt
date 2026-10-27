package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale

@Composable
fun SummaryScreen(
    packing: PackingState,
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null
) {
    val selected = packing.items.filter { it.quantity > 0 }
    val freeL = (packing.capacityL - packing.usedL).coerceAtLeast(0.0)
    val fill = if (packing.capacityL == 0.0) 0 else (packing.usedL / packing.capacityL * 100).toInt()
    val format: (Double) -> String = { String.format(Locale.US, "%.1f", it) }

    Column(modifier.fillMaxSize().background(PackGuideColors.background)) {
        Text("RESUMEN DEL EQUIPAJE", style = Typography.headlineMedium,
            color = PackGuideColors.primaryText, modifier = Modifier.padding(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                TechCard(headerTag = "MALETA SELECCIONADA", borderColor = PackGuideColors.primaryText) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Luggage, contentDescription = null, tint = AccentRedLed)
                        Text("  ${packing.preset.value.label} · ${packing.heightCm.value}×${packing.widthCm.value}×${packing.depthCm.value} cm",
                            color = PackGuideColors.primaryText, style = Typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Volumen exterior estimado: ${format(packing.capacityL)} L. Las dimensiones y límites reales dependen de la aerolínea.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                TechCard(headerTag = "CAPACIDAD ESTIMADA", borderColor = PackGuideColors.border) {
                    Text("${format(packing.usedL)} / ${format(packing.capacityL)} L · $fill%",
                        color = PackGuideColors.primaryText, style = Typography.headlineMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(if (packing.usedL > packing.capacityL) "Exceso estimado: ${format(packing.usedL - packing.capacityL)} L"
                        else "Espacio libre estimado: ${format(freeL)} L",
                        color = if (packing.usedL > packing.capacityL) AccentRedLed else PackGuideColors.primaryText)
                }
            }
            item {
                TechCard(headerTag = "ESTADO DE EMPACADO", borderColor = PackGuideColors.border) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = AccentGreenLed)
                        Text("  ${packing.packedUnits} empacados", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null,
                            tint = PackGuideColors.secondaryText)
                        Text("  ${packing.selectedUnits - packing.packedUnits} pendientes", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("${selected.size} tipos de artículos · ${packing.selectedUnits} unidades seleccionadas",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                Text("DETALLE POR ARTÍCULO", style = Typography.titleLarge,
                    color = PackGuideColors.primaryText)
            }
            item {
                TechCard(headerTag = "DISTRIBUCIÓN POR ZONA", borderColor = PackGuideColors.border) {
                    PackingZone.entries.forEach { zone ->
                        val zoneItems = selected.filter { it.zone == zone }
                        val units = zoneItems.sumOf { it.quantity }
                        val volume = zoneItems.sumOf { it.quantity * it.unitVolumeL }
                        Text("${zone.label}: $units unidades · ${format(volume)} L",
                            color = PackGuideColors.primaryText, style = Typography.bodyMedium)
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
            if (selected.isEmpty()) {
                item { Text("No hay artículos seleccionados.", color = PackGuideColors.secondaryText) }
            }
            items(selected, key = { it.id }) { item ->
                TechCard(borderColor = PackGuideColors.border) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(item.icon, contentDescription = null, tint = PackGuideColors.primaryText)
                        Text("  ${item.name}", modifier = Modifier.weight(1f),
                            color = PackGuideColors.primaryText, style = Typography.titleMedium)
                        Text("${item.packedQuantity}/${item.quantity}", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${item.zone.label} · ${format(item.quantity * item.unitVolumeL)} L estimados",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                AsciiButton("EDITAR CATÁLOGO", onClick = { onNavigateToTab?.invoke(NavTab.Catalog) },
                    modifier = Modifier.fillMaxWidth(), isPrimary = false,
                    icon = Icons.Outlined.Inventory2)
                Spacer(Modifier.height(8.dp))
                AsciiButton("CONTINUAR EMPACANDO", onClick = { onNavigateToTab?.invoke(NavTab.Guide) },
                    modifier = Modifier.fillMaxWidth(), isPrimary = true)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
