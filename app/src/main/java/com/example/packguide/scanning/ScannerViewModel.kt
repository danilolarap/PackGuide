package com.example.packguide.scanning

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import com.example.packguide.ui.screens.LuggagePreset

data class ScanState(
    val loading: Boolean = false,
    val photo: Bitmap? = null,
    val message: String? = null
)

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val states = mutableStateMapOf<LuggagePreset, ScanState>()
    private val jobs = mutableMapOf<LuggagePreset, Job>()
    fun stateFor(type: LuggagePreset): ScanState = states[type] ?: ScanState()
    private val scanner = PhotoScanner()

    fun message(text: String, type: LuggagePreset) { states[type] = stateFor(type).copy(message = text) }
    fun clearPhoto(type: LuggagePreset) {
        jobs.remove(type)?.cancel()
        states.remove(type)
    }
    fun clearPhotos() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        states.clear()
    }
    fun selectPhoto(uri: Uri, type: LuggagePreset) {
        if (stateFor(type).loading) return
        states[type] = stateFor(type).copy(loading = true, message = null)
        jobs[type] = viewModelScope.launch {
            try {
                val photo = scanner.decode(getApplication(), uri)
                states[type] = ScanState(photo = photo)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                states[type] = stateFor(type).copy(loading = false, message = "No se pudo abrir la foto. Elige otra imagen de la maleta.")
            } finally {
                if (jobs[type] == coroutineContext[Job]) jobs.remove(type)
            }
        }
    }

    fun selectPhoto(bitmap: Bitmap, type: LuggagePreset) {
        if (!stateFor(type).loading) states[type] = ScanState(photo = bitmap)
    }
}
