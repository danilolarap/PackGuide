package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun MeasurementsScreen(
    modifier: Modifier = Modifier,
    packing: PackingState,
    onOpenCatalog: () -> Unit
) {
    val copy = com.example.packguide.ui.localization.PackingText(com.example.packguide.ui.localization.LocalAppLanguage.current)
    var pendingRemoval by remember { mutableStateOf<LuggagePreset?>(null) }
    val remove = pendingRemoval
    if (remove != null) {
        AlertDialog(
            onDismissRequest = { pendingRemoval = null },
            title = { Text(copy.removeBagTitle(remove.label)) },
            text = { Text("Se quitarán del plan los artículos asignados a esta maleta. Las demás maletas conservarán sus artículos.") },
            confirmButton = { TextButton(onClick = {
                packing.toggleLuggage(remove)
                pendingRemoval = null
            }) { Text("QUITAR MALETA") } },
            dismissButton = { TextButton(onClick = { pendingRemoval = null }) { Text("CANCELAR") } }
        )
    }
    Column(modifier.fillMaxSize().background(PackGuideColors.background)) {
        com.example.packguide.ui.components.HudHeader(title = "MEDIDAS DEL EQUIPAJE", sysCode = "", statusText = "")
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                TripLuggageSelection(packing) { type ->
                    if (type in packing.selectedLuggage && packing.bag(type).selectedUnits > 0) {
                        pendingRemoval = type
                    } else {
                        packing.toggleLuggage(type)
                    }
                }
            }
            packing.selectedLuggage.forEach { type ->
                item(key = type.name) {
                    val bag = packing.bag(type)
                    TechCard(headerTag = copy.bagTitle(type.label)) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("MEDIDAS INTERIORES ÚTILES", style = Typography.labelMedium,
                                color = PackGuideColors.primaryText)
                            DimensionStepperRow("ALTO", bag.heightCm.value) {
                                bag.heightCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            DimensionStepperRow("ANCHO", bag.widthCm.value) {
                                bag.widthCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            DimensionStepperRow("PROF.", bag.depthCm.value) {
                                bag.depthCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            Text("${com.example.packguide.ui.localization.localizedText("CAPACIDAD INTERIOR TOTAL", com.example.packguide.ui.localization.LocalAppLanguage.current)}: ${com.example.packguide.ui.localization.formatLiters(bag.capacityL)} L",
                                style = Typography.titleMedium, color = PackGuideColors.primaryText)
                            Text("Mide el interior de tu maleta, ajusta los valores y confírmalos para calcular su capacidad.",
                                style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                            DimensionConfirmation(
                                modifier = Modifier.testTag("dimension-confirmation-${type.name}"),
                                confirmed = bag.dimensionsConfirmed.value,
                                onConfirm = { bag.dimensionsConfirmed.value = !bag.dimensionsConfirmed.value }
                            )
                        }
                    }
                }
            }
            item {
                Text("En Catálogo podrás repartir los artículos entre las maletas seleccionadas.",
                    style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                Spacer(Modifier.height(12.dp))
                AsciiButton("CONTINUAR AL CATÁLOGO", onClick = onOpenCatalog,
                    enabled = packing.allDimensionsConfirmed, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
internal fun DimensionConfirmation(confirmed: Boolean, modifier: Modifier = Modifier, onConfirm: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(if (confirmed) "LISTA PARA EMPACAR" else "CONFIRMACIÓN PENDIENTE",
            style = Typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(if (confirmed) "Si cambias una medida, deberás confirmarla de nuevo."
            else "Revisa las medidas y pulsa el botón para continuar al catálogo.",
            style = Typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
        AsciiButton(
            text = if (confirmed) "MEDIDAS CONFIRMADAS" else "CONFIRMAR MEDIDAS",
            icon = if (confirmed) Icons.Outlined.CheckCircle else Icons.Outlined.Straighten,
            onClick = onConfirm,
            isAccentRed = !confirmed,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
        )
    }
}

@Composable
fun DimensionStepperRow(label: String, valueCm: Int, onValueChange: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("$label:", modifier = Modifier.weight(1f), style = Typography.titleMedium,
            color = PackGuideColors.primaryText)
        androidx.compose.material3.IconButton(onClick = { onValueChange(valueCm - 1) }, enabled = valueCm > 1) {
            Text("−", color = PackGuideColors.primaryText)
        }
        Text("$valueCm CM", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        androidx.compose.material3.IconButton(onClick = { onValueChange(valueCm + 1) }, enabled = valueCm < 300) {
            Text("+", color = PackGuideColors.primaryText)
        }
    }
}
