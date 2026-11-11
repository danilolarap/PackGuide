package com.example.packguide

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.screens.PackingReportLayout
import com.example.packguide.ui.screens.PackingState
import com.example.packguide.ui.screens.PackingZone
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class PackingReportAppearanceTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun reportKeepsQuantitiesAndZonesInBothLanguagesAndThemes() {
        compose.runOnUiThread {
            compose.activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (android.os.Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true); compose.activity.setTurnScreenOn(true)
            }
        }
        val dark = mutableStateOf(false)
        val language = mutableStateOf(AppLanguage.Spanish)
        val packing = PackingState()
        packing.updateItem("ITEM-01") { it.copy(packedQuantity = 2) }
        packing.updateItem("ITEM-02") { it.copy(zone = PackingZone.Center) }
        compose.setContent {
            CompositionLocalProvider(LocalAppLanguage provides language.value) {
                PackGuideTheme(darkTheme = dark.value) {
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                        PackingReportLayout(packing.activeBag)
                    }
                }
            }
        }
        for (night in listOf(false, true)) for (locale in AppLanguage.entries) {
            compose.runOnIdle { dark.value = night; language.value = locale }
            compose.onNodeWithText(if (locale == AppLanguage.Spanish) "DISTRIBUCIÓN POR ZONAS" else "LAYOUT BY ZONE").assertIsDisplayed()
            compose.onNodeWithText(if (locale == AppLanguage.Spanish) "CAMISETAS" else "T-SHIRTS").assertExists()
            compose.onNodeWithText("×3").assertExists()
            val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
            File(compose.activity.filesDir, "report-${if (night) "dark" else "light"}-${locale.storageKey}.png")
                .outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
    }
}
