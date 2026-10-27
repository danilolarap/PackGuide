package com.example.packguide.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

data class CatalogItemData(
    val id: String,
    val name: String,
    val category: String,
    val dimensions: String,
    val unitVolumeL: Double,
    val icon: ImageVector,
    val quantity: Int,
    val packedQuantity: Int = 0,
    val zone: PackingZone = PackingZone.Base
)

enum class PackingZone(val label: String) {
    Base("BASE"), Center("CENTRO"), Top("ACCESO RÁPIDO");
    fun next(): PackingZone = entries[(ordinal + 1) % entries.size]
}

enum class LuggagePreset(val label: String, val heightCm: Int, val widthCm: Int, val depthCm: Int) {
    Personal("MANO", 45, 35, 20),
    Cabin("CABINA", 55, 35, 25),
    Checked("BODEGA", 75, 50, 30);

    val capacityL: Double get() = heightCm * widthCm * depthCm / 1000.0
}

class PackingState {
    val items = mutableStateListOf(
        CatalogItemData("ITEM-01", "CAMISETAS", "ROPA", "28x20x4 cm", 1.2, Icons.Outlined.Checkroom, 3),
        CatalogItemData("ITEM-02", "PANTALONES", "ROPA", "38x26x6 cm", 2.8, Icons.Outlined.DryCleaning, 2),
        CatalogItemData("ITEM-03", "CALZADO", "CALZADO", "32x22x12 cm", 4.5, Icons.AutoMirrored.Outlined.DirectionsWalk, 1),
        CatalogItemData("ITEM-04", "CHAQUETA", "ROPA", "40x28x8 cm", 3.2, Icons.Outlined.Checkroom, 1),
        CatalogItemData("ITEM-05", "NECESER", "ASEO", "22x14x8 cm", 1.5, Icons.Outlined.MedicalServices, 1),
        CatalogItemData("ITEM-06", "CARGADORES & TECH", "TECH", "18x12x4 cm", 0.8, Icons.Outlined.Devices, 1),
        CatalogItemData("ITEM-07", "ROPA INTERIOR", "ROPA", "18x12x3 cm", 0.3, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-08", "MEDIAS", "ROPA", "15x10x3 cm", 0.2, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-09", "PIJAMA", "ROPA", "30x22x5 cm", 1.4, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-10", "VESTIDO", "ROPA", "35x25x5 cm", 1.8, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-11", "FALDA", "ROPA", "28x20x4 cm", 1.0, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-12", "SHORTS", "ROPA", "28x20x4 cm", 1.0, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-13", "SUÉTER", "ROPA", "35x28x7 cm", 2.3, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-14", "CAMISA", "ROPA", "30x23x4 cm", 1.2, Icons.Outlined.Checkroom, 0),
        // ADD_ITEMS_HERE
    )

    var preset = mutableStateOf(LuggagePreset.Cabin)
    var heightCm = mutableStateOf(preset.value.heightCm)
    var widthCm = mutableStateOf(preset.value.widthCm)
    var depthCm = mutableStateOf(preset.value.depthCm)

    val capacityL: Double get() = heightCm.value * widthCm.value * depthCm.value / 1000.0
    val usedL: Double get() = items.sumOf { it.quantity * it.unitVolumeL }
    val packedUnits: Int get() = items.sumOf { it.packedQuantity }
    val selectedUnits: Int get() = items.sumOf { it.quantity }

    fun selectPreset(value: LuggagePreset) {
        preset.value = value
        heightCm.value = value.heightCm
        widthCm.value = value.widthCm
        depthCm.value = value.depthCm
    }

    fun updateItem(id: String, transform: (CatalogItemData) -> CatalogItemData) {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) items[index] = transform(items[index])
    }
}
