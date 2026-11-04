package com.example.packguide.ai

import com.example.packguide.ui.screens.LuggagePreset
import com.example.packguide.ui.screens.PackingState
import com.example.packguide.ui.screens.PackingZone
import org.junit.Assert.*
import org.junit.Test

class PackingPlanTest {
    @Test fun requestsIncludeOnlyTheSelectedBagsContents() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.updateItem("ITEM-01", LuggagePreset.Personal) { it.copy(quantity = 2, zone = PackingZone.Top) }
        val plan = PackingPlan.from(packing.bag(LuggagePreset.Personal))
        assertEquals(listOf(45, 35, 20), plan.dimensions)
        assertEquals(1, plan.contents.size)
        assertEquals(2, plan.contents.single().quantity)
        assertEquals(PackingZone.Top.label, plan.contents.single().zone)
        assertTrue(plan.description.contains("2 × CAMISETAS"))
        assertFalse(plan.description.contains("CALZADO"))
    }

    @Test fun snapshotsDetectLayoutChangesButIgnorePackingCheckmarks() {
        val packing = PackingState()
        val before = PackingPlan.from(packing.activeBag)
        packing.updateItem("ITEM-01") { it.copy(packedQuantity = 1) }
        assertEquals(before, PackingPlan.from(packing.activeBag))
        packing.updateItem("ITEM-01") { it.copy(zone = PackingZone.Top) }
        assertNotEquals(before, PackingPlan.from(packing.activeBag))
        packing.updateItem("ITEM-01") { it.copy(zone = PackingZone.Base, quantity = 4) }
        assertNotEquals(before, PackingPlan.from(packing.activeBag))
        packing.updateItem("ITEM-01") { it.copy(quantity = 3) }
        packing.heightCm.value += 1
        assertNotEquals(before, PackingPlan.from(packing.activeBag))
        assertEquals(55, before.dimensions.first())
        assertEquals(3, before.contents.first().quantity)
    }

    @Test fun previewIncludesEverySelectedTypeOnceAndRespectsZones() {
        val packing = PackingState()
        packing.items.toList().forEach { item -> packing.updateItem(item.id) { it.copy(quantity = 2) } }
        packing.updateItem("ITEM-01") { it.copy(zone = PackingZone.Top) }
        val plan = PackingPlan.from(packing.activeBag)
        val layout = PackingPreviewLayout.from(plan)
        assertEquals(listOf("ACCESO RÁPIDO", "CENTRO", "BASE"), layout.zones.map { it.label })
        assertEquals(36, layout.zones.sumOf { it.items.size })
        assertEquals(72, layout.zones.flatMap { it.items }.sumOf { it.quantity })
        assertEquals("CAMISETAS", layout.zones.first().items.single().name)
        assertEquals("ROPA", layout.zones.first().items.single().category)
        assertTrue(layout.height in 750..4096)
    }

    @Test fun unsupportedPreviewPlansAreRejectedInsteadOfReturningAnEmptySuccess() {
        val plan = PackingPlan.from(PackingState().activeBag)
        assertThrows(IllegalArgumentException::class.java) { PackingPreviewLayout.from(plan.copy(contents = emptyList())) }
        assertThrows(IllegalArgumentException::class.java) { PackingPreviewLayout.from(plan.copy(dimensions = listOf(0, 20, 20))) }
        assertThrows(IllegalArgumentException::class.java) {
            PackingPreviewLayout.from(plan.copy(contents = listOf(plan.contents.first().copy(zone = "UNKNOWN"))))
        }
    }
}
