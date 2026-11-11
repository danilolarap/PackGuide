package com.example.packguide

import androidx.activity.ComponentActivity
import android.os.Build
import android.view.WindowManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.screens.CatalogScreen
import com.example.packguide.ui.screens.GuideScreen
import com.example.packguide.ui.screens.PackingState
import com.example.packguide.ui.screens.SummaryScreen
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalizationScreenTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun englishScreensUseUppercaseHeadingsAndSearchUsesEnglishItemNames() {
        val packing = PackingState()
        val screen = mutableStateOf(0)
        compose.runOnUiThread {
            compose.activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true)
                compose.activity.setTurnScreenOn(true)
            }
        }
        compose.setContent {
            CompositionLocalProvider(LocalAppLanguage provides AppLanguage.English) {
                PackGuideTheme {
                    when (screen.value) {
                        0 -> CatalogScreen(packing = packing, onCreatePlan = {})
                        1 -> GuideScreen(packing = packing)
                        else -> SummaryScreen(packing = packing)
                    }
                }
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("ITEM CATALOG")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
        compose.onNodeWithText("ITEM CATALOG").assertIsDisplayed()
        compose.onNodeWithText("[CLOTHING]").assertExists()
        compose.onNode(hasSetTextAction()).performTextInput("SHIRT")
        compose.onNodeWithText("T-SHIRTS").assertExists()
        compose.onNodeWithText("PANTS").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Increase quantity: CARRY-ON")[0].assertExists()
        compose.runOnIdle { screen.value = 1 }
        compose.onNodeWithText("PACKING GUIDE").assertIsDisplayed()
        compose.onNodeWithText("CARRY-ON").assertExists()
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("ZONE: BOTTOM ↻"))
        compose.onAllNodesWithText("ZONE: BOTTOM ↻")[0].assertIsDisplayed()
        compose.runOnIdle { screen.value = 2 }
        compose.onNodeWithText("LUGGAGE SUMMARY").assertIsDisplayed()
        compose.onNodeWithText("ALL BAGS").assertExists()
        compose.onNodeWithText("BAG TO PACK").assertExists()
    }
}
