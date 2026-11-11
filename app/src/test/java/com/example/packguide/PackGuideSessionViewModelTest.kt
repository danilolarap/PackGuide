package com.example.packguide

import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.screens.LuggagePreset
import org.junit.Assert.*
import org.junit.Test

class PackGuideSessionViewModelTest {
    @Test fun navigationIncludesMeasurementsAfterSessionInitialization() {
        val session = PackGuideSessionViewModel()
        assertEquals(NavTab.Measurements, session.currentTab)
        assertEquals(listOf(NavTab.Measurements, NavTab.Catalog, NavTab.Guide, NavTab.Summary), NavTab.tabs)
    }

    @Test fun packingTabsRequireConfirmationOfEverySelectedBag() {
        val session = PackGuideSessionViewModel()
        session.packing.toggleLuggage(LuggagePreset.Personal)
        session.packing.bag(LuggagePreset.Cabin).dimensionsConfirmed.value = true
        listOf(NavTab.Catalog, NavTab.Guide, NavTab.Summary).forEach {
            session.navigateTo(it)
            assertEquals(NavTab.Measurements, session.currentTab)
        }
        assertFalse(session.canGoBack)
        session.packing.bag(LuggagePreset.Personal).dimensionsConfirmed.value = true
        session.navigateTo(NavTab.Catalog)
        assertEquals(NavTab.Catalog, session.currentTab)
        assertTrue(session.canGoBack)
    }

    @Test fun returningToMeasurementsAndChangingThemRequiresConfirmationAgain() {
        val session = PackGuideSessionViewModel()
        session.packing.dimensionsConfirmed.value = true
        session.navigateTo(NavTab.Catalog)
        session.navigateTo(NavTab.Guide)
        session.navigateTo(NavTab.Measurements)
        session.packing.dimensionsConfirmed.value = false
        assertFalse(session.canGoBack)
        session.navigateTo(NavTab.Summary)
        session.goBack()
        assertEquals(NavTab.Measurements, session.currentTab)
        session.packing.dimensionsConfirmed.value = true
        session.navigateTo(NavTab.Summary)
        assertEquals(NavTab.Summary, session.currentTab)
    }

    @Test fun selectingAnotherBagRequiresItsConfirmationAndLogoutResetsTheGate() {
        val session = PackGuideSessionViewModel()
        session.packing.dimensionsConfirmed.value = true
        session.navigateTo(NavTab.Measurements)
        session.packing.toggleLuggage(LuggagePreset.Checked)
        session.navigateTo(NavTab.Guide)
        assertEquals(NavTab.Measurements, session.currentTab)
        session.packing.bag(LuggagePreset.Checked).dimensionsConfirmed.value = true
        session.navigateTo(NavTab.Guide)
        assertEquals(NavTab.Guide, session.currentTab)
        session.logout()
        session.navigateTo(NavTab.Catalog)
        assertEquals(NavTab.Measurements, session.currentTab)
        assertFalse(session.canGoBack)
    }
}
