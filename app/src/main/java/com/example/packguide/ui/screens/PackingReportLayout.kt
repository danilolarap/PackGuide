package com.example.packguide.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.components.TravelDotIcon
import com.example.packguide.ui.components.TravelGlyph
import com.example.packguide.ui.components.dottedFrame
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.TechMonospaceFontFamily

@Composable
internal fun PackingTripReport(bags: List<LuggagePacking>) {
    val language = LocalAppLanguage.current
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(30.dp)) {
        Text(localizedText("EQUIPAJE DEL VIAJE", language), color = PackGuideColors.primaryText,
            fontSize = 17.sp, fontFamily = TechMonospaceFontFamily, fontWeight = FontWeight.Bold)
        bags.forEach { bag ->
            key(bag.type) { PackingReportLayout(bag) }
        }
    }
}

@Composable
internal fun PackingReportLayout(bag: LuggagePacking) {
    val language = LocalAppLanguage.current
    val words = PackingText(language)
    fun t(text: String) = localizedText(text, language)
    val dark = PackGuideColors.background.luminance() < .3f
    val ink = if (dark) Color.White else Color(0xFF181818)
    val muted = if (dark) Color(0xFFB8B8B8) else Color(0xFF555555)
    val surface = if (dark) Color(0xFF1B1C1A) else Color.White
    val tile = if (dark) Color(0xFF252624) else Color(0xFFF3F3F3)
    val fontScale = LocalDensity.current.fontScale
    val items = bag.items.filter { it.quantity > 0 }
    val backpack = bag.type == LuggagePreset.Personal
    val checked = bag.type == LuggagePreset.Checked
    val glyph = when (bag.type) {
        LuggagePreset.Personal -> TravelGlyph.Backpack
        LuggagePreset.Cabin -> TravelGlyph.Cabin
        LuggagePreset.Checked -> TravelGlyph.Hold
    }
    val radius = if (backpack) 38.dp else 26.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().testTag("packing-report-${bag.type.name}")
            .padding(horizontal = if (backpack) 10.dp else 0.dp)) {
        // Handle and wheels make the dotted frame read as an open luggage compartment.
        TravelDotIcon(glyph, Modifier.size(if (backpack) 76.dp else if (checked) 96.dp else 66.dp), accent = true)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.width(if (backpack) 42.dp else if (checked) 92.dp else 66.dp)
            .height(if (checked) 28.dp else 22.dp).dottedFrame(ink.copy(alpha = .65f), dark))
        Column(Modifier.fillMaxWidth().background(surface, RoundedCornerShape(radius))
            .dottedFrame(ink.copy(alpha = .55f), dark, radius).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TravelDotIcon(glyph, Modifier.size(if (checked) 50.dp else 42.dp), accent = true)
                Column(Modifier.weight(1f)) {
                    Text(t("DISTRIBUCIÓN POR ZONAS"), color = ink, fontSize = 15.sp,
                        fontFamily = TechMonospaceFontFamily, fontWeight = FontWeight.Bold)
                    Text(words.bagTitle(bag.type.label), color = ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("${bag.heightCm.value} × ${bag.widthCm.value} × ${bag.depthCm.value} cm", color = muted, fontSize = 12.sp)
                }
            }
            Text(words.progress(bag.packedUnits, bag.selectedUnits, bag.progressPercent), color = muted, fontSize = 12.sp)
            ReportDotMeter(bag.progressPercent, ink.copy(alpha = .12f))
            listOf(PackingZone.Top, PackingZone.Center, PackingZone.Base).forEachIndexed { index, zone ->
                val assigned = items.filter { it.zone == zone }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(30.dp).background(ink, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                            Text("0${index + 1}", color = surface, fontSize = 12.sp, fontFamily = TechMonospaceFontFamily)
                        }
                        Text(t(zone.label), color = ink, fontSize = 13.sp,
                            fontFamily = TechMonospaceFontFamily, fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f))
                        Text(words.units(assigned.sumOf { it.quantity }), color = muted, fontSize = 11.sp)
                    }
                    if (assigned.isEmpty()) {
                        Text(t("Sin artículos asignados"), color = muted, fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().background(tile, RoundedCornerShape(14.dp)).padding(14.dp))
                    }
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val columns = if (maxWidth < 300.dp || fontScale > 1.3f) 1 else 2
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            assigned.chunked(columns).forEach { row ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    row.forEach { item ->
                                        Column(Modifier.weight(1f).background(tile, RoundedCornerShape(16.dp)).padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(item.icon, null, tint = ink, modifier = Modifier.size(20.dp))
                                                Text("×${item.quantity}", color = AccentRedLed, fontSize = 20.sp,
                                                    fontFamily = TechMonospaceFontFamily, fontWeight = FontWeight.Bold)
                                            }
                                            Text(t(item.name), color = ink, fontSize = 12.sp, lineHeight = 17.sp,
                                                fontWeight = FontWeight.Medium)
                                            Text(item.dimensions, color = muted, fontSize = 11.sp, lineHeight = 16.sp)
                                        }
                                    }
                                    if (row.size < columns) Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
            if (backpack) {
                Box(Modifier.fillMaxWidth().height(38.dp)
                    .dottedFrame(ink.copy(alpha = .35f), dark, 12.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxWidth(.65f).height(4.dp)) {
                        repeat(24) { dot -> drawCircle(ink.copy(alpha = .4f), 1.dp.toPx(),
                            Offset((dot + .5f) * size.width / 24, size.height / 2)) }
                    }
                }
            }
        }
        if (!backpack) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 36.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(Modifier.width(22.dp).height(12.dp).background(ink, RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)))
                Box(Modifier.width(22.dp).height(12.dp).background(ink, RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)))
            }
        }
    }
}

@Composable
private fun ReportDotMeter(percent: Int, inactive: Color) {
    Canvas(Modifier.fillMaxWidth().height(20.dp)) {
        val active = (percent.coerceIn(0, 100) * 40 / 100)
        val step = size.width / 20
        repeat(40) { index ->
            drawCircle(if (index < active) AccentRedLed else inactive,
                radius = minOf(2.dp.toPx(), step * .2f),
                center = Offset((index % 20 + .5f) * step, (index / 20 + .5f) * size.height / 2))
        }
    }
}
