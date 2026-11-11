package com.example.packguide.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.packguide.ai.PackingPreviewState
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.localizedText

private val frame = Color(0xFF1C1C1B)
private val paper = Color(0xFFF3F4F7)
private val ink = Color(0xFF202020)
private val mutedInk = Color(0xFF606060)
private val rule = Color(0xFFCACCD0)

@Composable
internal fun OrganizedPackingDialog(
    bag: LuggagePacking,
    photo: Bitmap?,
    state: PackingPreviewState,
    canAssess: Boolean,
    onAssess: () -> Unit,
    onCancel: () -> Unit,
    onClose: () -> Unit
) {
    val language = LocalAppLanguage.current
    val words = PackingText(language)
    fun t(value: String) = localizedText(value, language)
    val items = bag.items.filter { it.quantity > 0 }
    val estimate = bag.estimate
    val assessment = photo?.let { state.assessments[it to language] }
    val feedbackParts = assessment?.feedback?.split("\n\n", limit = 2)

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxWidth().fillMaxHeight(0.94f).padding(horizontal = 10.dp).background(frame)) {
            val topSpace = (maxHeight * 0.13f).coerceAtMost(106.dp)
            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(topSpace))
                Column(Modifier.weight(1f).fillMaxWidth().background(paper).verticalScroll(rememberScrollState()).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(t("VISTA PREVIA DE ORGANIZACIÓN"), color = ink, fontSize = 19.sp)
                        Text(t("Composición local · foto original e inventario"), color = mutedInk, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            val shape = RoundedCornerShape(6.dp)
                            Box(Modifier.fillMaxWidth().height(120.dp).background(Color.White, shape)
                                .border(1.dp, rule, shape).padding(4.dp), contentAlignment = Alignment.Center) {
                                if (photo == null) Text(t("Sin foto de referencia"), color = mutedInk, fontSize = 12.sp)
                                else Image(photo.asImageBitmap(), t("FOTO DE REFERENCIA"), Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                            }
                            Text(t("FOTO DE REFERENCIA"), color = mutedInk, fontSize = 11.sp)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(t("INVENTARIO SELECCIONADO"), color = ink, fontSize = 12.sp)
                            Text(words.units(bag.selectedUnits), color = ink, fontSize = 14.sp)
                            Text(words.volume(estimate), color = ink, fontSize = 14.sp)
                            Text(t("Estimación / capacidad interior"), color = mutedInk, fontSize = 11.sp)
                            Text(words.balance(estimate), color = ink, fontSize = 12.sp)
                            Text("${bag.heightCm.value} × ${bag.widthCm.value} × ${bag.depthCm.value} cm", color = ink, fontSize = 13.sp)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(assessment?.score?.let { "${t("PUNTUACIÓN DE LA IA")} · $it/10" }
                            ?: t("PUNTUACIÓN DE LA IA"), color = ink, fontSize = 13.sp)
                        val assessmentMessage = when {
                            photo == null -> "Añade una foto de tu maleta para recibir una puntuación y sugerencias."
                            state.work == com.example.packguide.ai.PreviewWork.Assessment -> "La IA está evaluando la foto…"
                            !canAssess -> "La evaluación no está configurada en esta versión de la aplicación."
                            else -> "Foto lista para evaluar. Pulsa EVALUAR FOTO CON IA para obtener tu puntuación."
                        }
                        Text(feedbackParts?.firstOrNull() ?: t(assessmentMessage),
                            color = mutedInk, fontSize = 13.sp)
                        if (photo != null && assessment == null) {
                            Text(t("Al evaluar, se enviará solo la foto que elijas al servicio de IA."), color = mutedInk, fontSize = 11.sp)
                            Button(onClick = onAssess, enabled = canAssess && state.work == null,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ink, contentColor = Color.White)) {
                                Text(t("EVALUAR FOTO CON IA"))
                            }
                        }
                    }
                    if (state.work != null || state.problem != null) {
                        PreviewStatus(state, onCancel, textColor = ink)
                    }
                    feedbackParts?.getOrNull(1)?.let { tips ->
                        Text(tips, color = mutedInk, fontSize = 13.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.size(38.dp, 8.dp).border(1.dp, ink, RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)))
                        val shape = RoundedCornerShape(10.dp)
                        Column(Modifier.fillMaxWidth().background(Color.White, shape).border(1.dp, ink, shape).padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (zone in listOf(PackingZone.Top, PackingZone.Center, PackingZone.Base)) {
                                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(t(zone.label), color = ink, fontSize = 13.sp)
                                    val assigned = items.filter { it.zone == zone }
                                    if (assigned.isEmpty()) Text(t("Sin artículos asignados"), color = mutedInk, fontSize = 12.sp)
                                    for (row in assigned.chunked(2)) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            for (item in row) {
                                                val tile = RoundedCornerShape(5.dp)
                                                Row(Modifier.weight(1f).background(paper, tile).border(1.dp, rule, tile).padding(6.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(item.icon, null, Modifier.size(16.dp), tint = ink)
                                                    Column {
                                                        Text("${item.quantity} × ${t(item.name)}", color = ink, fontSize = 11.sp, lineHeight = 14.sp)
                                                        Text(item.dimensions, color = mutedInk, fontSize = 10.sp, lineHeight = 13.sp)
                                                    }
                                                }
                                            }
                                            if (row.size == 1) Spacer(Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Text(t("Distribución ilustrativa. El esquema no mide ni garantiza el ajuste físico."), color = mutedInk, fontSize = 11.sp)
                }
                TextButton(onClick = onClose, modifier = Modifier.fillMaxWidth().heightIn(min = 68.dp)) {
                    Text(t("CERRAR"), color = Color.White, fontSize = 26.sp, letterSpacing = 3.sp)
                }
            }
        }
    }
}
