package com.example.packguide

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.ui.screens.*
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PackingVolumeScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun catalogGuideAndSummaryShowTheSameCapacityAndBalance() {
        val packing = PackingState()
        listOf(5, 4, 3, 3, 2, 1).forEachIndexed { index, count ->
            packing.updateItem("ITEM-0${index + 1}") { it.copy(quantity = count) }
        }
        val tab = mutableStateOf(0)
        compose.setContent {
            PackGuideTheme {
                when (tab.value) {
                    0 -> CatalogScreen(packing = packing, onCreatePlan = {})
                    1 -> GuideScreen(packing = packing)
                    else -> SummaryScreen(packing = packing)
                }
            }
        }
        compose.onNodeWithText("44.1 L / 48.1 L").assertExists()
        compose.onNodeWithText("ESPACIO EST.: 4.0 L").assertExists()
        compose.runOnIdle { tab.value = 1 }
        compose.onNodeWithText("44.1 L / 48.1 L").assertExists()
        compose.onNodeWithText("ESPACIO EST.: 4.0 L; comprueba el ajuste al empacar.").assertExists()
        compose.runOnIdle { packing.heightCm.value = 44 }
        compose.onNodeWithText("44.1 L / 38.5 L").assertExists()
        compose.onNodeWithText("EXCESO EST.: 5.6 L; reduce artículos o elige una maleta mayor.").assertExists()
        compose.runOnIdle { tab.value = 0 }
        compose.onNodeWithText("44.1 L / 38.5 L").assertExists()
        compose.onNodeWithText("EXCESO EST.: 5.6 L").assertExists()
        compose.runOnIdle { tab.value = 2 }
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("EXCESO EST.: 5.6 L"))
        compose.onNodeWithText("44.1 L / 38.5 L").assertExists()
        compose.onNodeWithText("EXCESO EST.: 5.6 L").assertExists()
    }
}
