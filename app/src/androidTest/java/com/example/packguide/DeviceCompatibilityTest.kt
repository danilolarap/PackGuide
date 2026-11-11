package com.example.packguide

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.pressBack
import androidx.test.platform.app.InstrumentationRegistry
import com.example.packguide.ai.PackingPreviewViewModel
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.screens.LuggagePreset
import org.junit.Assert.*
import org.junit.Test

class DeviceCompatibilityTest {
    @Test fun activityRecreationRetainsSessionInventoryPhotosAndDestination() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            lateinit var original: PackGuideSessionViewModel
            val photo = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
            scenario.onActivity { activity ->
                original = ViewModelProvider(activity)[PackGuideSessionViewModel::class.java]
                original.isAuthenticated = true
                original.packing.updateItem("ITEM-01") { it.copy(quantity = 7) }
                original.packing.dimensionsConfirmed.value = true
                original.navigateTo(NavTab.Catalog)
                ViewModelProvider(activity)[PackingPreviewViewModel::class.java].addPhoto(LuggagePreset.Cabin, photo)
            }
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.recreate()
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.onActivity { activity ->
                val session = ViewModelProvider(activity)[PackGuideSessionViewModel::class.java]
                assertSame(original, session)
                assertTrue(session.isAuthenticated)
                assertEquals(7, session.packing.items.first { it.id == "ITEM-01" }.quantity)
                assertEquals(NavTab.Catalog, session.currentTab)
                assertSame(photo, ViewModelProvider(activity)[PackingPreviewViewModel::class.java].stateFor(LuggagePreset.Cabin).photos.single())
                assertFalse(photo.isRecycled)
            }
            pressBack()
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
            scenario.onActivity { activity ->
                assertEquals(NavTab.Measurements, ViewModelProvider(activity)[PackGuideSessionViewModel::class.java].currentTab)
                assertFalse(activity.isFinishing)
            }
        }
    }
}
