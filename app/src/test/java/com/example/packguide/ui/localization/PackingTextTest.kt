package com.example.packguide.ui.localization

import com.example.packguide.scanning.PackingEstimate
import com.example.packguide.settings.AppLanguage
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class PackingTextTest {
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
