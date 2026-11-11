package com.example.packguide.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun LuggageSelector(packing: PackingState) {
    Column {
        Text("MALETA A ORGANIZAR", style = Typography.labelMedium, color = PackGuideColors.secondaryText)
        FlowRow(Modifier.fillMaxWidth().selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            packing.selectedLuggage.forEach { type ->
                FilterChip(
                    selected = packing.activeLuggage == type,
                    onClick = { packing.selectLuggage(type) },
                    label = { Text(type.label, style = Typography.labelSmall) }
                )
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
                            Icon(
                                imageVector = when (type) {
                                    LuggagePreset.Personal -> Icons.Outlined.Backpack
                                    LuggagePreset.Cabin -> Icons.Outlined.WorkOutline
                                    LuggagePreset.Checked -> Icons.Outlined.Luggage
                                },
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(24.dp)
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
