package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.packguide.ui.theme.AccentRedLed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.components.travelDotBackground
import com.example.packguide.ui.theme.Typography
import kotlinx.coroutines.flow.first

@Composable
fun GuideScreen(
    packing: PackingState,
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null
) {
    val copy = PackingText(LocalAppLanguage.current)
    var playingTutorial by remember { mutableStateOf<LuggagePreset?>(null) }
    val listState = rememberLazyListState()
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { playingTutorial = null }
    LaunchedEffect(playingTutorial) {
        val type = playingTutorial ?: return@LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.any { it.key == "${type.name}-tutorial" } }
            .first { !it }
        playingTutorial = null
    }

    Column(modifier.fillMaxSize().travelDotBackground(3)) {
        com.example.packguide.ui.components.HudHeader(title = "GUÍA DE EMPACADO", sysCode = "", statusText = "")
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Mira el tutorial de cada maleta y marca las unidades que ya guardaste.",
                    style = Typography.bodyMedium, color = PackGuideColors.secondaryText)
            }
            packing.selectedLuggage.forEach { type ->
                val bag = packing.bag(type)
                item(key = "${type.name}-heading") {
                    Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Outlined.Luggage, contentDescription = null, tint = PackGuideColors.primaryText)
                        Text(type.label, style = Typography.titleLarge, color = PackGuideColors.primaryText)
                    }
                }
                item(key = "${type.name}-tutorial") {
                    PackingTutorialPanel(type, playingTutorial == type,
                        onPlay = { playingTutorial = type },
                        onClose = { if (playingTutorial == type) playingTutorial = null })
                }
                item(key = "${type.name}-progress") {
                    TechCard(headerTag = "PROGRESO REAL") {
                        Text(copy.progress(bag.packedUnits, bag.selectedUnits, bag.progressPercent),
                            style = Typography.titleMedium, color = PackGuideColors.primaryText)
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { bag.progressPercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = PackGuideColors.primaryText,
                            trackColor = PackGuideColors.surfaceVariant,
                            drawStopIndicator = {}
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Marca cada unidad cuando esté dentro de esta maleta. Puedes desmarcarla si la sacas.",
                            style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                    }
                }
                val selectedItems = bag.items.filter { it.quantity > 0 }
                if (selectedItems.isEmpty()) {
                    item(key = "${type.name}-empty") {
                        TechCard {
                            Text("Aún no hay artículos seleccionados. Añádelos en el catálogo.",
                                color = PackGuideColors.secondaryText)
                            Spacer(Modifier.height(12.dp))
                            AsciiButton("IR AL CATÁLOGO", onClick = {
                                packing.selectLuggage(type)
                                onNavigateToTab?.invoke(NavTab.Catalog)
                            }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                selectedItems.forEach { catalogItem ->
                    item(key = "${type.name}-${catalogItem.id}-heading") {
                        Column(Modifier.padding(top = 16.dp, bottom = 12.dp)) {
                        TechCard {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(catalogItem.icon, contentDescription = null, tint = PackGuideColors.primaryText)
                            Text(catalogItem.name, modifier = Modifier.weight(1f), style = Typography.titleMedium,
                                color = PackGuideColors.primaryText)
                            Text("${catalogItem.packedQuantity}/${catalogItem.quantity}", color = PackGuideColors.secondaryText)
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            packing.updateItem(catalogItem.id, type) { it.copy(zone = it.zone.next()) }
                        }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PackGuideColors.primaryText,
                                contentColor = PackGuideColors.surface),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
                        ) {
                            Icon(Icons.Outlined.SwapVert, contentDescription = null,
                                tint = AccentRedLed, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(12.dp))
                            Text(copy.zoneAction(catalogItem.zone.label),
                                style = Typography.labelMedium, color = PackGuideColors.surface)
                        }
                        }
                        }
                    }
                    items(count = catalogItem.quantity, key = { unit -> "${type.name}-${catalogItem.id}-$unit" }) { unit ->
                        val checked = bag.isUnitPacked(catalogItem.id, unit)
                        Row(
                            modifier = Modifier.fillMaxWidth().toggleable(
                                value = checked, role = Role.Checkbox,
                                onValueChange = { bag.setUnitPacked(catalogItem.id, unit, it) }
                            ).padding(horizontal = 8.dp, vertical = 4.dp).heightIn(min = 48.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Checkbox(checked = checked, onCheckedChange = null)
                            Column(Modifier.weight(1f)) {
                                Text(catalogItem.name, style = Typography.bodyMedium,
                                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (checked) PackGuideColors.secondaryText else PackGuideColors.primaryText)
                                Text("${unit + 1} / ${catalogItem.quantity}", style = Typography.bodySmall,
                                    color = PackGuideColors.secondaryText)
                            }
                            Text(if (checked) "EMPACADO" else "PENDIENTE", style = Typography.labelSmall,
                                color = PackGuideColors.secondaryText)
                        }
                        HorizontalDivider(color = PackGuideColors.border)
                    }
                }
            }
            item {
                AsciiButton("VER RESUMEN", onClick = { onNavigateToTab?.invoke(NavTab.Summary) },
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
