package com.example.packguide.ui.localization

import com.example.packguide.scanning.PackingEstimate
import com.example.packguide.settings.AppLanguage
import java.util.Locale

/** One decimal separator/precision across every packing screen. Round only for display. */
fun formatLiters(value: Double): String =
    if (value > 0 && value < 0.05) "<0.1" else String.format(Locale.US, "%.1f", value)

class PackingText(private val language: AppLanguage) {
    private fun choose(es: String, en: String) = if (language == AppLanguage.English) en else es
    private fun unitWord(count: Int) = choose(if (count == 1) "unidad" else "unidades", if (count == 1) "unit" else "units")
    fun units(count: Int) = "$count ${unitWord(count)}"
    fun volume(estimate: PackingEstimate) = "${formatLiters(estimate.usedL)} L / ${formatLiters(estimate.capacityL)} L"
    fun balance(estimate: PackingEstimate): String = if (estimate.exceedsCapacity)
        choose("EXCESO EST.: ${formatLiters(estimate.excessL)} L", "EST. EXCESS: ${formatLiters(estimate.excessL)} L")
    else choose("ESPACIO EST.: ${formatLiters(estimate.freeL)} L", "EST. FREE SPACE: ${formatLiters(estimate.freeL)} L")
    fun recommendation(estimate: PackingEstimate) = balance(estimate) + "; " + if (estimate.exceedsCapacity)
        choose("reduce artículos o elige una maleta mayor.", "reduce items or choose a larger bag.")
    else choose("comprueba el ajuste al empacar.", "check the fit while packing.")
    fun itemTotal(count: Int, volume: Double) = "TOTAL: $count ${unitWord(count)} · ${formatLiters(volume)} L"
    fun counts(selected: Int, packed: Int) = choose(
        "$selected ${unitWord(selected)} ${if (selected == 1) "seleccionada" else "seleccionadas"} · $packed ${if (packed == 1) "empacada" else "empacadas"}",
        "$selected ${unitWord(selected)} selected · $packed packed")
    fun progress(packed: Int, selected: Int, percent: Int) = choose(
        "$packed de $selected ${unitWord(selected)} ${if (selected == 1) "empacada" else "empacadas"} · $percent%",
        "$packed of $selected ${unitWord(selected)} packed · $percent%")
    fun selectedBag(label: String) = choose("MALETA SELECCIONADA: ", "SELECTED BAG: ") + localizedText(label, language)
    fun itemEstimate(volume: Double) = choose("ESTIMACIÓN POR UNIDAD: ", "ESTIMATE PER UNIT: ") + "${formatLiters(volume)} L"
    fun itemGeometry(volume: Double) = choose("VOLUMEN RECTANGULAR DE REFERENCIA: ", "REFERENCE RECTANGULAR VOLUME: ") + "${formatLiters(volume)} L"
    fun bagSummary(label: String, estimate: PackingEstimate, packed: Int, selected: Int) =
        "${localizedText(label, language)} · ${volume(estimate)} · $packed/$selected " + choose("empacados", "packed")
    fun occupancy(percent: Int) = choose("OCUPACIÓN EST.: $percent%", "EST. OCCUPANCY: $percent%")
    fun zone(label: String, units: Int, volume: Double) = "${localizedText(label, language)}: $units " +
        unitWord(units) + " · ${formatLiters(volume)} L"

    fun bagTitle(label: String) = choose("MALETA: ", "BAG: ") + localizedText(label, language)
    fun removeBagTitle(label: String) = choose("QUITAR MALETA: ", "REMOVE BAG: ") + localizedText(label, language)
    fun zoneAction(label: String) = choose("ZONA: ", "ZONE: ") + localizedText(label, language) + " ↻"
    fun zoneVolume(label: String, volume: Double) = "${localizedText(label, language)} · ${formatLiters(volume)} L"
    fun bagDimensions(label: String, height: Int, width: Int, depth: Int) =
        "${localizedText(label, language)} · $height×$width×$depth cm"
    fun packedCount(count: Int) = choose("$count ${if (count == 1) "empacado" else "empacados"}", "$count packed")
    fun pendingCount(count: Int) = choose("$count ${if (count == 1) "pendiente" else "pendientes"}", "$count remaining")
    fun tripTotal(selected: Int, packed: Int) = choose(
        "TOTAL: $selected ${unitWord(selected)} · $packed ${if (packed == 1) "empacada" else "empacadas"}",
        "TOTAL: $selected ${unitWord(selected)} · $packed packed")
    fun itemKinds(types: Int, selected: Int) = choose(
        "$types ${if (types == 1) "tipo de artículo" else "tipos de artículos"} · $selected ${unitWord(selected)} ${if (selected == 1) "seleccionada" else "seleccionadas"}",
        "$types ${if (types == 1) "item type" else "item types"} · $selected ${unitWord(selected)} selected")
}
