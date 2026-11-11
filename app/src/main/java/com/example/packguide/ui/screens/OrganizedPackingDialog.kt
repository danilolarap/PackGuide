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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.packguide.ai.PackingPreviewState
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.components.TravelDotIcon
import com.example.packguide.ui.components.TravelGlyph
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.AccentRedLed

@Composable
internal fun OrganizedPackingDialog(
    bag: LuggagePacking,
    photo: Bitmap?,
    state: PackingPreviewState,
    canAssess: Boolean,
    onAssess: () -> Unit,
    onCancel: () -> Unit,
    onClose: () -> Unit,
    selectedBags: List<LuggagePacking> = listOf(bag)
) {
    val language = LocalAppLanguage.current
    val words = PackingText(language)
    fun t(value: String) = localizedText(value, language)
    val dark = PackGuideColors.background.luminance() < .3f
    val frame = if (dark) Color(0xFF101110) else Color(0xFF181818)
    val paper = if (dark) Color(0xFF101110) else Color(0xFFF6F6F6)
    val ink = if (dark) Color.White else Color(0xFF181818)
    val mutedInk = if (dark) Color(0xFFB8B8B8) else Color(0xFF555555)
    val rule = ink.copy(alpha = .18f)
    val surface = if (dark) Color(0xFF1B1C1A) else Color.White
    val estimate = bag.estimate
    val assessment = photo?.let { state.assessments[it to language] }
    val feedbackParts = assessment?.feedback?.split("\n\n", limit = 2)

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        BoxWithConstraints(Modifier.fillMaxWidth().fillMaxHeight(0.94f).padding(horizontal = 10.dp).background(frame)) {
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    TravelDotIcon(TravelGlyph.Plane, Modifier.size(28.dp), accent = true)
                    Text("PACKGUIDE / ${t("INFORME DE EQUIPAJE")}", color = Color.White,
                        fontSize = 11.sp, fontFamily = TechMonospaceFontFamily)
                }
                Column(Modifier.weight(1f).fillMaxWidth().background(paper).verticalScroll(rememberScrollState()).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(t("VISTA PREVIA DE ORGANIZACIÓN"), color = ink, fontSize = 21.sp,
                            fontFamily = TechMonospaceFontFamily)
                        Text(t("Composición local · foto original e inventario"), color = mutedInk, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            val shape = RoundedCornerShape(6.dp)
                            Box(Modifier.fillMaxWidth().height(140.dp).background(surface, shape)
                                .border(1.dp, rule, shape).padding(4.dp), contentAlignment = Alignment.Center) {
                                if (photo == null) Text(t("Sin foto de referencia"), color = mutedInk, fontSize = 12.sp)
                                else Image(photo.asImageBitmap(), t("FOTO DE REFERENCIA"), Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                            }
                            Text(t("FOTO DE REFERENCIA"), color = mutedInk, fontSize = 11.sp)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(t("INVENTARIO SELECCIONADO"), color = ink, fontSize = 12.sp)
                            Text(words.bagTitle(bag.type.label), color = ink, fontSize = 12.sp)
                            Text(words.units(bag.selectedUnits), color = ink, fontSize = 14.sp)
                            Text(words.volume(estimate), color = ink, fontSize = 14.sp)
                            Text(t("Estimación / capacidad interior"), color = mutedInk, fontSize = 11.sp)
                            Text(words.balance(estimate), color = ink, fontSize = 12.sp)
                            Text("${bag.heightCm.value} × ${bag.widthCm.value} × ${bag.depthCm.value} cm", color = ink, fontSize = 13.sp)
                        }
                    }

                    Column(Modifier.fillMaxWidth().background(surface, RoundedCornerShape(20.dp)).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(assessment?.score?.let { "${t("PUNTUACIÓN DE LA IA")} · $it/10" }
                            ?: t("PUNTUACIÓN DE LA IA"), color = AccentRedLed, fontSize = 16.sp,
                            fontFamily = TechMonospaceFontFamily)
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
                                colors = ButtonDefaults.buttonColors(containerColor = ink, contentColor = surface)) {
                                Text(t("EVALUAR FOTO CON IA"))
                            }
                        }
                    }
                    if (state.work != null || state.problem != null) {
                        PreviewStatus(state, onCancel, textColor = ink)
                    }
                    feedbackParts?.getOrNull(1)?.let { tips ->
                        Text(tips, color = ink, fontSize = 13.sp, lineHeight = 21.sp,
                            modifier = Modifier.fillMaxWidth().background(surface, RoundedCornerShape(20.dp)).padding(16.dp))
                    }

                    PackingTripReport(selectedBags)
                    Text(t("Distribución ilustrativa. El esquema no mide ni garantiza el ajuste físico."), color = mutedInk, fontSize = 11.sp)
                }
                TextButton(onClick = onClose, modifier = Modifier.fillMaxWidth().heightIn(min = 68.dp)) {
                    Text(t("CERRAR"), color = Color.White, fontSize = 26.sp, letterSpacing = 3.sp)
                }
            }
        }
    }
}
