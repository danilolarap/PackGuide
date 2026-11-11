package com.example.packguide.ui.screens

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.packguide.ai.*
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
private fun previewText(es: String) = localizedText(es, LocalAppLanguage.current)

@Composable
internal fun PreviewStatus(state: PackingPreviewState, onCancel: () -> Unit, textColor: Color = PackGuideColors.primaryText) {
    state.work?.let { work ->
        CircularProgressIndicator(color = textColor)
        Text(when (work) {
            PreviewWork.Photos -> previewText("Abriendo fotos…")
            PreviewWork.Review -> previewText("La IA está generando una propuesta…")
            PreviewWork.Assessment -> previewText("La IA está evaluando la foto…")
        },
            color = textColor, style = Typography.bodySmall)
        TextButton(onClick = onCancel) { Text(previewText("CANCELAR"), color = textColor) }
    }
    state.problem?.let { problem ->
        Text(when (problem) {
            PreviewProblem.Photo -> previewText("No se pudo abrir la foto o la cámara. Intenta elegir otra imagen.")
            PreviewProblem.UnsupportedPhoto -> previewText("Ese archivo no es una foto compatible. Elige una imagen JPG, PNG o WebP.")
            PreviewProblem.LargePhoto -> previewText("La foto es demasiado grande. Elige una imagen más pequeña.")
            PreviewProblem.Generation -> previewText("No se pudo generar la foto con IA. Inténtalo de nuevo.")
            PreviewProblem.Assessment -> previewText("No se pudo evaluar la foto con IA. Inténtalo de nuevo.")
            PreviewProblem.Unavailable -> previewText("La generación con IA todavía no está disponible.")
            PreviewProblem.NotConfigured -> previewText("El servidor de imágenes necesita una clave de OpenAI.")
            PreviewProblem.LocalConnection -> previewText("La evaluación está sin conexión. Puedes seguir consultando el esquema y la técnica de organización.")
            PreviewProblem.ProviderAccess -> previewText("OpenAI rechazó el acceso. Revisa la clave y los permisos del modelo en el servidor.")
            PreviewProblem.ProviderQuota -> previewText("La cuenta de OpenAI del servidor no tiene cuota disponible para generar imágenes.")
            PreviewProblem.Timeout -> previewText("La solicitud de imagen superó el tiempo de espera. Inténtalo de nuevo.")
            PreviewProblem.Busy -> previewText("El servicio de IA está ocupado. Inténtalo de nuevo en un momento.")
            PreviewProblem.Connection -> previewText("El servicio no está disponible. Inténtalo de nuevo más tarde.")
            PreviewProblem.Cancelled -> previewText("Operación cancelada. Puedes volver a intentarlo.")
        }, color = if (problem in setOf(PreviewProblem.Cancelled, PreviewProblem.LocalConnection)) textColor else MaterialTheme.colorScheme.error,
            style = Typography.bodySmall)
    }
}

@Composable
fun PackingReviewPreviewPanel(packing: PackingState, preview: PackingPreviewViewModel) {
    val type = packing.activeLuggage
    val state = preview.stateFor(type)
    val language = LocalAppLanguage.current
    key(type) {
        var showLayout by remember { mutableStateOf(false) }
        var reference by remember { mutableStateOf<Bitmap?>(null) }
        if (showLayout) OrganizedPackingDialog(packing.activeBag, reference, state, preview.isAvailable,
            onAssess = { reference?.let { preview.assess(type, it, language) } },
            onCancel = { preview.cancel(type) }, onClose = { showLayout = false })
        val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(4)) { uris -> preview.addPhotos(type, uris) }
        val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo -> if (photo != null) preview.addPhoto(type, photo) }
        TechCard(headerTag = previewText("INSPIRACIÓN PARA TU MALETA")) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.photos.isNotEmpty()) Text(previewText("TU ORGANIZACIÓN"),
                    style = Typography.labelMedium, color = PackGuideColors.primaryText)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.photos.size) { index ->
                        val photo = state.photos[index]
                        Column(Modifier.width(180.dp)) {
                            Image(photo.asImageBitmap(), contentDescription = previewText("Foto para comparar"),
                                contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth().height(150.dp))
                            TextButton(onClick = { reference = photo; showLayout = true }) {
                                Text(previewText("VER ORGANIZACI?N"))
                            }
                            TextButton(onClick = { preview.removePhoto(type, index) }, enabled = state.work == null) { Text(previewText("QUITAR FOTO")) }
                        }
                    }
                }
                AsciiButton(previewText("AÑADIR FOTOS"), onClick = {
                    try { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    catch (_: ActivityNotFoundException) { preview.photoError(type) }
                    catch (_: SecurityException) { preview.photoError(type) }
                }, enabled = state.photos.size < 4 && state.work == null, isPrimary = false, modifier = Modifier.fillMaxWidth())
                AsciiButton(previewText("TOMAR FOTO"), onClick = {
                    try { camera.launch(null) }
                    catch (_: ActivityNotFoundException) { preview.photoError(type) }
                    catch (_: SecurityException) { preview.photoError(type) }
                }, enabled = state.photos.size < 4 && state.work == null, isPrimary = false, modifier = Modifier.fillMaxWidth())
                AsciiButton(previewText("VER VISTA PREVIA"), onClick = {
                    reference = state.photos.firstOrNull()
                    showLayout = true
                }, modifier = Modifier.fillMaxWidth())
                if (!showLayout) PreviewStatus(state, onCancel = { preview.cancel(type) })
            }
        }
    }
}
