package com.example.packguide.ui.localization

import com.example.packguide.settings.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.Locale
import com.example.packguide.ui.screens.LuggagePreset
import com.example.packguide.ui.screens.PackingState
import com.example.packguide.ui.screens.PackingZone

class AppTextTest {
    @Test fun spanishKeepsOriginalTextAndExactEnglishTranslationWins() {
        assertEquals("CATÁLOGO", localizedText("CATÁLOGO", AppLanguage.Spanish))
        assertEquals("CONTINUE TO CATALOG", localizedText("CONTINUAR AL CATÁLOGO", AppLanguage.English))
    }

    @Test fun allScreenHeadersUseCompleteUppercaseEnglishPhrases() {
        mapOf(
            "MEDIDAS DEL EQUIPAJE" to "LUGGAGE DIMENSIONS",
            "CATÁLOGO DE ÍTEMS" to "ITEM CATALOG",
            "GUÍA DE EMPACADO" to "PACKING GUIDE",
            "RESUMEN DEL EQUIPAJE" to "LUGGAGE SUMMARY"
        ).forEach { (es, en) -> assertEquals(en, localizedText(es, AppLanguage.English)) }
    }

    @Test fun uppercaseLabelsKeepTheirCaseThroughoutThePhraseCatalog() {
        englishTranslations.keys.filter { source ->
            source.any(Char::isLetter) && source.filter(Char::isLetter).all(Char::isUpperCase)
        }.forEach { source ->
            val translated = localizedText(source, AppLanguage.English)
            assertEquals(source, translated.uppercase(Locale.ROOT), translated)
        }
    }

    @Test fun decorationSpacingAndSentenceCaseArePreserved() {
        assertEquals("[ CLOTHING ]", localizedText("[ ROPA ]", AppLanguage.English))
        assertEquals("  T-SHIRTS", localizedText("  CAMISETAS", AppLanguage.English))
        assertEquals("DEPTH:", localizedText("PROF.:", AppLanguage.English))
        assertEquals("Catalog", localizedText("Catálogo", AppLanguage.English))
        assertEquals("catalog", localizedText("catálogo", AppLanguage.English))
        assertEquals("Internet connection required.", localizedText("Requiere conexión a internet.", AppLanguage.English))
    }

    @Test fun unknownWordsIdentifiersAndEnglishTextAreNeverPartiallyTranslated() {
        listOf("MY-ItemS-123", "EQUIPAJE-2026", "CATALOG OF ITEMS", "DE ItemS", "ZX-123").forEach {
            assertEquals(it, localizedText(it, AppLanguage.English))
        }
    }

    @Test fun everyCatalogItemBagAndZoneHasACompleteEnglishLabel() {
        val labels = PackingState().catalogItems.flatMap { listOf(it.name, it.category) } +
            LuggagePreset.entries.map { it.label } + PackingZone.entries.map { it.label }
        labels.forEach {
            assertFalse("Missing translation: $it", it !in englishTranslations)
        }
    }

    @Test fun casingDoesNotDependOnThePhonesSystemLocale() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            assertEquals("ITEM CATALOG", localizedText("CATÁLOGO DE ÍTEMS", AppLanguage.English))
            assertEquals("LUGGAGE DIMENSIONS", localizedText("MEDIDAS DEL EQUIPAJE", AppLanguage.English))
        } finally { Locale.setDefault(previous) }
    }
}
