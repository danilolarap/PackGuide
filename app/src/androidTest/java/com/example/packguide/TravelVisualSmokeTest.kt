package com.example.packguide

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import com.example.packguide.data.auth.AuthRepository
import com.example.packguide.data.auth.PackGuideDatabase
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.screens.*
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class TravelVisualSmokeTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun travelScreensRemainReadableInBothThemes() {
        compose.runOnUiThread {
            compose.activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            compose.activity.setShowWhenLocked(true)
            compose.activity.setTurnScreenOn(true)
        }
        val dark = mutableStateOf(false)
        val screen = mutableStateOf(0)
        val packing = PackingState()
        val database = Room.inMemoryDatabaseBuilder(compose.activity, PackGuideDatabase::class.java).build()
        try {
            val repository = AuthRepository(database.userAccountDao())
            compose.setContent {
                PackGuideTheme(darkTheme = dark.value) {
                    when (screen.value) {
                        0 -> AuthScreen(repository, AppSettings(isDarkTheme = dark.value), {}, {}, {})
                        1 -> MeasurementsScreen(packing = packing, onOpenCatalog = {})
                        2 -> CatalogScreen(packing = packing, onCreatePlan = {})
                        3 -> GuideScreen(packing = packing)
                        else -> SummaryScreen(packing = packing)
                    }
                }
            }
            val headings = listOf("INICIAR SESIÓN", "MEDIDAS DEL EQUIPAJE", "CATÁLOGO DE ÍTEMS", "GUÍA DE EMPACADO", "RESUMEN DEL EQUIPAJE")
            for (night in listOf(false, true)) {
                headings.forEachIndexed { index, heading ->
                    compose.runOnIdle { dark.value = night; screen.value = index }
                    compose.onNodeWithText(heading).assertIsDisplayed()
                    val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
                    File(compose.activity.filesDir, "travel-${if (night) "dark" else "light"}-$index.png")
                        .outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                }
            }
        } finally { database.close() }
    }
}
