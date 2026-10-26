package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.components.HudHeader
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.components.TechBadge
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.BackgroundLight
import com.example.packguide.ui.theme.BorderSlate
import com.example.packguide.ui.theme.CardSurfaceLight
import com.example.packguide.ui.theme.PrimaryBlack
import com.example.packguide.ui.theme.SecondarySlate
import com.example.packguide.ui.theme.SurfaceVariantLight
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography
import java.util.Locale

data class CatalogItemData(
    val id: String,
    val name: String,
    val category: String,
    val dimensions: String,
    val unitVolumeL: Double,
    val icon: ImageVector,
    var quantity: Int,
)

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ROPA") }

    val categories = listOf("ROPA", "CALZADO", "ASEO", "TECH", "TODOS")

    // Items list initialized with exact specification defaults
    val itemsList = remember {
        mutableStateListOf(
            CatalogItemData(
                id = "ITEM-01",
                name = "CAMISETAS",
                category = "ROPA",
                dimensions = "28x20x4 cm",
                unitVolumeL = 1.2,
                icon = Icons.Outlined.Checkroom,
                quantity = 3
            ),
            CatalogItemData(
                id = "ITEM-02",
                name = "PANTALONES",
                category = "ROPA",
                dimensions = "38x26x6 cm",
                unitVolumeL = 2.8,
                icon = Icons.Outlined.DryCleaning,
                quantity = 2
            ),
            CatalogItemData(
                id = "ITEM-03",
                name = "CALZADO",
                category = "CALZADO",
                dimensions = "32x22x12 cm",
                unitVolumeL = 4.5,
                icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                quantity = 1,
            ),
            CatalogItemData(
                id = "ITEM-04",
                name = "CHAQUETA",
                category = "ROPA",
                dimensions = "40x28x8 cm",
                unitVolumeL = 3.2,
                icon = Icons.Outlined.Checkroom,
                quantity = 1
            ),
            CatalogItemData(
                id = "ITEM-05",
                name = "NECESER",
                category = "ASEO",
                dimensions = "22x14x8 cm",
                unitVolumeL = 1.5,
                icon = Icons.Outlined.MedicalServices,
                quantity = 1
            ),
            CatalogItemData(
                id = "ITEM-06",
                name = "CARGADORES & TECH",
                category = "TECH",
                dimensions = "18x12x4 cm",
                unitVolumeL = 0.8,
                icon = Icons.Outlined.Devices,
                quantity = 1
            )
        )
    }

    val filteredItems = itemsList.filter { item ->
        val matchesCategory = (selectedCategory == "TODOS") || (item.category == selectedCategory)
        val matchesQuery = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true) || item.category.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    // Dynamic Volume Calculations
    val totalCapacityL = 38.0
    val totalUsedVolumeL = itemsList.sumOf { item -> item.quantity * item.unitVolumeL }
    val availableVolumeL = maxOf(0.0, totalCapacityL - totalUsedVolumeL)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // --- 1. TOP BAR: NOTHING (R) // SYS CATÁLOGO DE ÍTEMS ---
        HudHeader(
            title = "CATÁLOGO DE ÍTEMS",
            sysCode = "NOTHING (R) // SYS",
            statusText = "[ SYS ONLINE ]",
            ledColor = AccentGreenLed
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            // --- 2. SEARCH INPUT FIELD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurfaceLight, shape = RoundedCornerShape(2.dp))
                    .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(2.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = PrimaryBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "BUSCAR ÍTEM... [ _ ]",
                                style = Typography.bodyMedium,
                                fontFamily = TechMonospaceFontFamily,
                                color = SecondarySlate
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = Typography.bodyMedium.copy(
                                color = PrimaryBlack,
                                fontFamily = TechMonospaceFontFamily
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TechBadge(
                        text = "ASCII",
                        containerColor = SurfaceVariantLight,
                        contentColor = PrimaryBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 3. CATEGORY FILTER CHIPS ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isActive = selectedCategory == category
                    val chipText = if (isActive) "[• $category]" else "[$category]"

                    Box(
                        modifier = Modifier
                            .background(
                                if (isActive) PrimaryBlack else CardSurfaceLight,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .border(
                                1.dp,
                                if (isActive) AccentRedLed else BorderSlate,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isActive) {
                                LedIndicator(color = AccentRedLed, size = 5.dp, isPulsing = true)
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = chipText,
                                style = Typography.labelSmall.copy(fontSize = 11.sp),
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (isActive) CardSurfaceLight else PrimaryBlack
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 4. ITEM INVENTORY LIST ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    val itemIndex = itemsList.indexOfFirst { it.id == item.id }
                    val itemSubtotalL = item.quantity * item.unitVolumeL

                    TechCard(
                        borderColor = BorderSlate
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Column: Icon & Info
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(SurfaceVariantLight, shape = RoundedCornerShape(4.dp))
                                        .border(1.dp, BorderSlate, shape = RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name,
                                        tint = PrimaryBlack,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.name,
                                            style = Typography.titleMedium,
                                            fontFamily = TechMonospaceFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryBlack
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        TechBadge(
                                            text = item.category,
                                            containerColor = SurfaceVariantLight,
                                            contentColor = PrimaryBlack
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "${item.dimensions}  |  ${item.unitVolumeL}L",
                                        style = Typography.labelSmall,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = SecondarySlate
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Subtotal in RED
                                    Text(
                                        text = "SUBTOTAL: ${String.format(Locale.US, "%.1f", itemSubtotalL)}L",
                                        style = Typography.labelMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentRedLed
                                    )
                                }
                            }

                            // Right Column: Interactive Counter Buttons [- qty +]
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Decrement button (-)
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(CardSurfaceLight, shape = RoundedCornerShape(2.dp))
                                        .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(2.dp))
                                        .clickable {
                                            if ((itemIndex != -1) && (itemsList[itemIndex].quantity > 0)) {
                                                itemsList[itemIndex] = itemsList[itemIndex].copy(
                                                    quantity = itemsList[itemIndex].quantity - 1
                                                )
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "-",
                                        style = Typography.titleMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = PrimaryBlack
                                    )
                                }

                                // Quantity display
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(32.dp)
                                        .background(SurfaceVariantLight, shape = RoundedCornerShape(2.dp))
                                        .border(1.dp, BorderSlate, shape = RoundedCornerShape(2.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.quantity.toString(),
                                        style = Typography.titleMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlack
                                    )
                                }

                                // Increment button (+)
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(PrimaryBlack, shape = RoundedCornerShape(2.dp))
                                        .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(2.dp))
                                        .clickable {
                                            if (itemIndex != -1) {
                                                itemsList[itemIndex] = itemsList[itemIndex].copy(
                                                    quantity = itemsList[itemIndex].quantity + 1
                                                )
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        style = Typography.titleMedium,
                                        fontFamily = TechMonospaceFontFamily,
                                        color = CardSurfaceLight
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // --- 5. BOTTOM CAPACITY TRACKER CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(4.dp))
                    .background(CardSurfaceLight, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, PrimaryBlack, shape = RoundedCornerShape(4.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Metric header & available badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VOLUMEN USADO: ${String.format(Locale.US, "%.1f", totalUsedVolumeL)}L / ${String.format(Locale.US, "%.1f", totalCapacityL)}L",
                            style = Typography.titleMedium,
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlack
                        )

                        TechBadge(
                            text = "CAPACIDAD DISPONIBLE: ${String.format(Locale.US, "%.1f", availableVolumeL)}L",
                            containerColor = PrimaryBlack,
                            contentColor = CardSurfaceLight,
                            ledColor = AccentGreenLed
                        )
                    }

                    // Visual Segmented Progress Bar (10 block segments)
                    val filledSegmentsCount = ((totalUsedVolumeL / totalCapacityL) * 10).toInt().coerceIn(0, 10)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (i in 1..10) {
                            val isFilled = i <= filledSegmentsCount
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(14.dp)
                                    .background(
                                        if (isFilled) AccentRedLed else SurfaceVariantLight,
                                        shape = RoundedCornerShape(1.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isFilled) AccentRedLed else BorderSlate,
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }

                    // Primary Action Button: "CREAR PLAN DE EMPACADO ->"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryBlack, shape = RoundedCornerShape(20.dp))
                            .border(1.dp, AccentRedLed, shape = RoundedCornerShape(20.dp))
                            .clickable { /* Create packing plan action */ }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CREAR PLAN DE EMPACADO",
                                style = Typography.titleMedium,
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CardSurfaceLight
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = null,
                                tint = AccentRedLed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
