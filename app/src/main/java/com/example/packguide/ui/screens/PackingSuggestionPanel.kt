package com.example.packguide.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.scanning.PackingRules
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale

@Composable
fun PackingSuggestionPanel(packing: PackingState) {
    val estimate = PackingRules.estimate(packing.heightCm.value, packing.widthCm.value, packing.depthCm.value,
        packing.items.map { it.quantity to it.unitVolumeL })
    var expanded by rememberSaveable { mutableStateOf(false) }
    TechCard(headerTag = "SUGERENCIA ORIENTATIVA") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Estimación de artículos / espacio orientativo:", color = PackGuideColors.secondaryText,
                style = Typography.bodySmall)
            androidx.compose.material3.Text(
                String.format(Locale.US, "%.1f L / %.1f L", estimate.usedL, estimate.usableL),
                color = if (estimate.exceedsAllowance) AccentRedLed else PackGuideColors.primaryText,
                style = Typography.displaySmall)
            if (!packing.dimensionsConfirmed.value) Text(
                "Confirma las medidas en Escáner antes de usar esta estimación.",
                color = PackGuideColors.secondaryText, style = Typography.bodySmall)
            Text(if (estimate.exceedsAllowance) "Reduce cantidades o usa una maleta mayor: el volumen estimado supera el espacio orientativo."
                else "El volumen estimado está dentro del margen orientativo; comprueba el ajuste al empacar.",
                color = PackGuideColors.primaryText, style = Typography.bodySmall)
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "OCULTAR DETALLES" else "VER CRITERIOS Y CONSEJOS",
                    color = PackGuideColors.primaryText, style = Typography.labelMedium)
            }
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Regla: alto × ancho × fondo / 1000; reserva del 20% para forro, curvas y huecos. Usa medidas interiores útiles.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text("Los litros por artículo son supuestos del catálogo, no medidas de la foto. No es una simulación física ni garantiza que todo quepa.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text("Calzado en bolsas junto a las ruedas; pantalones y chaquetas en la base. Enrolla camisas resistentes en el centro; vestidos delicados arriba. Accesorios en un bolsillo accesible.",
                        color = PackGuideColors.primaryText, style = Typography.bodySmall)
                }
            }
        }
    }
}