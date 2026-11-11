package com.example.packguide.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.components.TravelDotIcon
import com.example.packguide.ui.components.TravelGlyph

@Composable
fun LuggageSelector(packing: PackingState) {
    val words = PackingText(LocalAppLanguage.current)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("MALETA A ORGANIZAR", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        Text("Toca una maleta para ver su resumen y evaluar sus fotos.",
            style = Typography.bodySmall, color = PackGuideColors.secondaryText)
        Column(Modifier.fillMaxWidth().selectableGroup(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            packing.selectedLuggage.forEach { type ->
                val selected = packing.activeLuggage == type
                val bag = packing.bag(type)
                val background by animateColorAsState(
                    if (selected) PackGuideColors.primaryText else PackGuideColors.surface,
                    label = "SummaryBagBackground")
                val foreground = if (selected) PackGuideColors.surface else PackGuideColors.primaryText
                Surface(
                    modifier = Modifier.fillMaxWidth().testTag("summary-bag-${type.name}")
                        .selectable(selected = selected, role = Role.RadioButton,
                            onClick = { packing.selectLuggage(type) }),
                    color = background, shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(if (selected) 2.dp else 1.dp,
                        if (selected) AccentRedLed else PackGuideColors.border),
                    shadowElevation = if (selected) 3.dp else 0.dp
                ) {
                    Row(Modifier.heightIn(min = 86.dp).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        TravelDotIcon(when (type) {
                            LuggagePreset.Personal -> TravelGlyph.Backpack
                            LuggagePreset.Cabin -> TravelGlyph.Cabin
                            LuggagePreset.Checked -> TravelGlyph.Hold
                        }, modifier = Modifier.size(40.dp), accent = selected)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(type.label, style = Typography.titleMedium, color = foreground)
                            Text(words.progress(bag.packedUnits, bag.selectedUnits, bag.progressPercent),
                                style = Typography.bodySmall, color = foreground)
                        }
                        Box(Modifier.size(28.dp)
                            .background(if (selected) AccentRedLed else background, CircleShape)
                            .border(1.dp, if (selected) AccentRedLed else PackGuideColors.border, CircleShape),
                            contentAlignment = Alignment.Center) {
                            if (selected) Icon(Icons.Outlined.Check, null,
                                tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripLuggageSelection(packing: PackingState, onToggle: (LuggagePreset) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("EQUIPAJE DEL VIAJE", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        Text("Selecciona todas las maletas que llevarás. Debe quedar al menos una.",
            style = Typography.bodySmall, color = PackGuideColors.secondaryText)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LuggagePreset.entries.forEach { type ->
                val selected = type in packing.selectedLuggage
                val colors = MaterialTheme.colorScheme
                val background by animateColorAsState(
                    if (selected) colors.primaryContainer else colors.surface,
                    label = "luggageBackground"
                )
                val border by animateColorAsState(
                    if (selected) colors.primary else colors.outlineVariant,
                    label = "luggageBorder"
                )
                val contentColor = if (selected) colors.onPrimaryContainer else colors.onSurface
                val shape = RoundedCornerShape(24.dp)
                Surface(
                    modifier = Modifier.fillMaxWidth().clip(shape).toggleable(
                        value = selected,
                        enabled = !selected || packing.selectedLuggage.size > 1,
                        role = Role.Checkbox,
                        onValueChange = { onToggle(type) }
                    ),
                    shape = shape,
                    color = background,
                    border = BorderStroke(if (selected) 1.5.dp else 1.dp, border)
                ) {
                    Row(
                        modifier = Modifier.heightIn(min = 64.dp).padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).background(
                                contentColor.copy(alpha = 0.08f), CircleShape
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            TravelDotIcon(
                                glyph = when (type) {
                                    LuggagePreset.Personal -> TravelGlyph.Backpack
                                    LuggagePreset.Cabin -> TravelGlyph.Cabin
                                    LuggagePreset.Checked -> TravelGlyph.Hold
                                },
                                accent = selected,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(type.label, modifier = Modifier.weight(1f),
                            style = Typography.titleMedium, color = contentColor)
                        Box(
                            modifier = Modifier.size(24.dp).background(
                                if (selected) colors.primary else colors.surfaceVariant, CircleShape
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) {
                                Icon(Icons.Outlined.Check, contentDescription = null,
                                    modifier = Modifier.size(16.dp), tint = colors.onPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
