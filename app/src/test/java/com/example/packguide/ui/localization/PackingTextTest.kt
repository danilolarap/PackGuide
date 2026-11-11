package com.example.packguide.ui.localization

import com.example.packguide.scanning.PackingEstimate
import com.example.packguide.settings.AppLanguage
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class PackingTextTest {
    @Test fun dynamicLabelsTranslateBagNamesZonesAndCountsWithoutChangingNumbers() {
        val en = PackingText(AppLanguage.English)
        assertEquals("BAG: CARRY-ON", en.bagTitle("CABINA"))
        assertEquals("REMOVE BAG: CHECKED BAG", en.removeBagTitle("BODEGA"))
        assertEquals("ZONE: QUICK ACCESS ↻", en.zoneAction("ACCESO RÁPIDO"))
        assertEquals("PERSONAL ITEM · 45×35×20 cm", en.bagDimensions("MANO", 45, 35, 20))
        assertEquals("MIDDLE · 4.5 L", en.zoneVolume("CENTRO", 4.5))
        assertEquals("TOTAL: 3 units · 2 packed", en.tripTotal(3, 2))
        assertEquals("2 item types · 3 units selected", en.itemKinds(2, 3))
    }

    @Test fun singleUnitsAndZeroUseNaturalGrammarInBothLanguages() {
        val es = PackingText(AppLanguage.Spanish)
        val en = PackingText(AppLanguage.English)
        assertEquals("0 de 1 unidad empacada · 0%", es.progress(0, 1, 0))
        assertEquals("0 of 1 unit packed · 0%", en.progress(0, 1, 0))
        assertEquals("1 tipo de artículo · 1 unidad seleccionada", es.itemKinds(1, 1))
        assertEquals("1 item type · 1 unit selected", en.itemKinds(1, 1))
        assertEquals("TOTAL: 1 unidad · 1 empacada", es.tripTotal(1, 1))
        assertEquals("TOTAL: 0 units · 0 packed", en.tripTotal(0, 0))
        assertEquals("1 pendiente", es.pendingCount(1))
        assertEquals("1 remaining", en.pendingCount(1))
    }

    @Test fun sameFormattingAndBalancesInBothLanguages() {
        val es = PackingText(AppLanguage.Spanish)
        val en = PackingText(AppLanguage.English)
        val fits = PackingEstimate(48.125, 44.1)
        val exceeds = PackingEstimate(38.5, 44.1)
        assertEquals("44.1 L / 48.1 L", es.volume(fits))
        assertEquals(es.volume(fits), en.volume(fits))
        assertEquals("ESPACIO EST.: 4.0 L", es.balance(fits))
        assertEquals("EXCESO EST.: 5.6 L", es.balance(exceeds))
        assertEquals("EST. FREE SPACE: 4.0 L", en.balance(fits))
        assertEquals("EST. EXCESS: 5.6 L", en.balance(exceeds))
        assertTrue(en.recommendation(exceeds).contains("choose a larger bag"))
        assertEquals("4 of 18 units packed · 22%", en.progress(4, 18, 22))
        assertEquals("ESTIMATE PER UNIT: 2.8 L", en.itemEstimate(2.8))
    }

    @Test fun systemLocaleDoesNotChangePrecisionAndSmallExcessIsNotDisplayedAsZero() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.GERMANY)
            assertEquals("48.1", formatLiters(48.125))
            assertEquals("0.0", formatLiters(0.0))
            assertEquals("<0.1", formatLiters(0.001))
            assertEquals("EST. EXCESS: <0.1 L", PackingText(AppLanguage.English).balance(PackingEstimate(48.125, 48.126)))
        } finally { Locale.setDefault(previous) }
    }
}
