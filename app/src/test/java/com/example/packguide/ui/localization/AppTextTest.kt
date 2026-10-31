package com.example.packguide.ui.localization

import com.example.packguide.settings.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class AppTextTest {
    @Test fun spanishKeepsOriginalTextAndExactEnglishTranslationWins() {
        assertEquals("CATÁLOGO", localizedText("CATÁLOGO", AppLanguage.Spanish))
        assertEquals("CONTINUE TO CATALOG", localizedText("CONTINUAR AL CATÁLOGO", AppLanguage.English))
    }

    @Test fun dynamicLabelsRetainNumbersAndReplaceTermsCaseInsensitively() {
        assertEquals("Luggage: 2 / Clothing: 7", localizedText("EQUIPAJE: 2 / ROPA: 7", AppLanguage.English))
        assertEquals("Packed: 3", localizedText("Empacado: 3", AppLanguage.English))
        assertEquals("ZX-123", localizedText("ZX-123", AppLanguage.English))
    }
}
