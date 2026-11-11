package com.example.packguide

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.ui.screens.*
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PackingControlsTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun dimensionsHaveAVisibleConfirmationActionAndStatus() {
        val confirmed = mutableStateOf(false)
        compose.setContent {
            PackGuideTheme {
                DimensionConfirmation(confirmed.value) { confirmed.value = !confirmed.value }
            }
        }
        compose.onNodeWithText("CONFIRMACIÓN PENDIENTE").assertIsDisplayed()
        compose.onNodeWithText("CONFIRMAR MEDIDAS").performClick()
        compose.onNodeWithText("MEDIDAS CONFIRMADAS").assertIsDisplayed()
        compose.onNodeWithText("MEDIDAS CONFIRMADAS").performClick()
        compose.onNodeWithText("CONFIRMACIÓN PENDIENTE").assertIsDisplayed()
    }

    @Test fun allCapacityBarsStayVisibleWhenTheInventoryScrolls() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.toggleLuggage(LuggagePreset.Checked)
        compose.setContent { PackGuideTheme { CatalogScreen(packing = packing, onCreatePlan = {}) } }
        compose.onNodeWithText("MALETA A ORGANIZAR").assertDoesNotExist()
        LuggagePreset.entries.forEach { compose.onNodeWithTag("capacity-${it.name}").assertIsDisplayed() }
        compose.onAllNodesWithContentDescription("Aumentar cantidad: MANO")[0].performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, packing.bag(LuggagePreset.Personal).selectedUnits) }
        compose.onNodeWithTag("catalog-inventory").performScrollToNode(hasText("RELOJ"))
        LuggagePreset.entries.forEach { compose.onNodeWithTag("capacity-${it.name}").assertIsDisplayed() }
    }
}
