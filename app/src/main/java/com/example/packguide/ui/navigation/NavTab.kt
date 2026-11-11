package com.example.packguide.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavTab(
    val route: String,
    val title: String,
    val asciiLabel: String,
    val icon: ImageVector,
    val sysCode: String
) {
    object Measurements : NavTab(
        route = "measurements",
        title = "[MEDIDAS]",
        asciiLabel = "[ MEDIDAS ]",
        icon = Icons.Outlined.Straighten,
        sysCode = "SYS // DIMENSIONS"
    )

    object Catalog : NavTab(
        route = "catalog",
        title = "[CATÁLOGO]",
        asciiLabel = "[ CATÁLOGO ]",
        icon = Icons.Outlined.Inventory2,
        sysCode = "SYS // INVENTORY_DB"
    )

    object Guide : NavTab(
        route = "guide",
        title = "[GUÍA]",
        asciiLabel = "[ GUÍA ]",
        icon = Icons.Outlined.Checklist,
        sysCode = "SYS // PACKING_GUIDE"
    )

    object Summary : NavTab(
        route = "summary",
        title = "[RESUMEN]",
        asciiLabel = "[ RESUMEN ]",
        icon = Icons.Outlined.Analytics,
        sysCode = "SYS // OVERVIEW_METRICS"
    )

    companion object {
        // Defer the list until the first tab object has finished initializing.
        val tabs: List<NavTab> by lazy { listOf(Measurements, Catalog, Guide, Summary) }
    }
}
