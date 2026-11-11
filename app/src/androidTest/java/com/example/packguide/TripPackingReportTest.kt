package com.example.packguide

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.packguide.ai.PackingPreviewViewModel
import com.example.packguide.ai.RemotePackingImageService
import com.example.packguide.ui.screens.*
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test

class TripPackingReportTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun everyLuggageCombinationAppearsInPreviewWithIndependentQuantitiesInBothThemes() {
        compose.runOnUiThread {
            compose.activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (android.os.Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true); compose.activity.setTurnScreenOn(true)
            }
        }
        val state = mutableStateOf(PackingState())
        val dark = mutableStateOf(false)
        val preview = PackingPreviewViewModel(compose.activity.application as Application, RemotePackingImageService(""))
        compose.setContent { PackGuideTheme(darkTheme = dark.value) { PackingReviewPreviewPanel(state.value, preview) } }
        for (night in listOf(false, true)) for (mask in 1..7) {
            val selected = LuggagePreset.entries.filterIndexed { index, _ -> mask and (1 shl index) != 0 }
            val packing = PackingState()
            selected.filter { it !in packing.selectedLuggage }.forEach { packing.toggleLuggage(it) }
            packing.selectedLuggage.filter { it !in selected }.forEach { packing.toggleLuggage(it) }
            selected.forEach { type ->
                packing.bag(type).items.toList().forEach { item ->
                    packing.updateItem(item.id, type) {
                        it.copy(quantity = if (item.id == "ITEM-01") type.ordinal + 2 else 0, packedQuantity = 0)
                    }
                }
            }
            packing.selectLuggage(selected.last())
            compose.runOnIdle { state.value = packing; dark.value = night }
            compose.onNodeWithText("VER VISTA PREVIA").performClick()
            LuggagePreset.entries.forEach { type ->
                val report = "packing-report-${type.name}"
                if (type in selected) {
                    compose.onNodeWithTag(report).assertExists()
                    compose.onNode(hasText("×${type.ordinal + 2}") and hasAnyAncestor(hasTestTag(report))).assertExists()
                } else compose.onNodeWithTag(report).assertDoesNotExist()
            }
            compose.onNodeWithText("CERRAR").performClick()
        }
        compose.runOnIdle { preview.clear() }
    }
}
