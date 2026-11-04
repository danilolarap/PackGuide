package com.example.packguide.ui.screens

import org.junit.Assert.*
import org.junit.Test

class PackingVolumeTest {
    @Test fun defaultInventoryHasUniqueItemsAndEverySelectedItemContributesOnce() {
        val packing = PackingState()
        assertEquals(36, packing.catalogItems.size)
        assertEquals(36, packing.catalogItems.map { it.id }.distinct().size)
        assertEquals(listOf("ITEM-01", "ITEM-02", "ITEM-03", "ITEM-04", "ITEM-05", "ITEM-06"),
            packing.items.filter { it.quantity > 0 }.map { it.id })
        assertEquals(9, packing.selectedUnits)
        assertEquals(19.2, packing.usedL, 0.000001)
        packing.items.toList().forEach { item -> packing.updateItem(item.id) { it.copy(packedQuantity = it.quantity) } }
        assertEquals(19.2, packing.usedL, 0.000001)
        assertEquals(9, packing.packedUnits)
        assertEquals(100, packing.activeBag.progressPercent)
    }

    @Test fun referenceDimensionsDoNotPretendToBeMeasuredPackingVolumes() {
        val catalog = PackingState().catalogItems
        val trousers = catalog.first { it.id == "ITEM-02" }
        val shoes = catalog.first { it.id == "ITEM-03" }
        assertEquals("38 × 26 × 6 cm", trousers.dimensions)
        assertEquals(5.928, trousers.dimensionsCm.rectangularVolumeL, 0.000001)
        assertEquals(2.8, trousers.unitVolumeL, 0.0)
        assertEquals(8.448, shoes.dimensionsCm.rectangularVolumeL, 0.000001)
        assertEquals(4.5, shoes.unitVolumeL, 0.0)
        assertEquals(11.2, trousers.copy(quantity = 4).totalVolumeL, 0.000001)
        assertEquals(13.5, shoes.copy(quantity = 3).totalVolumeL, 0.000001)
    }

    @Test fun eighteenUnitRegressionFixtureHasConsistentCapacityBalanceAndProgress() {
        // One possible inventory matching the totals; the screenshots do not identify all quantities.
        val packing = PackingState()
        val quantities = listOf(5, 4, 3, 3, 2, 1)
        quantities.forEachIndexed { index, count -> packing.updateItem("ITEM-0${index + 1}") { it.copy(quantity = count) } }
        assertEquals(18, packing.selectedUnits)
        assertEquals(44.1, packing.usedL, 0.000001)
        val estimate = packing.activeBag.estimate
        assertEquals(48.125, estimate.capacityL, 0.000001)
        assertEquals(4.025, estimate.freeL, 0.000001)
        assertFalse(estimate.exceedsCapacity)
        packing.updateItem("ITEM-02") { it.copy(packedQuantity = 4) }
        assertEquals(22, packing.activeBag.progressPercent)
        assertEquals(44.1, packing.usedL, 0.000001)
        packing.heightCm.value = 44 // Same real inventory, smaller confirmed interior envelope.
        assertEquals(38.5, packing.capacityL, 0.000001)
        assertEquals(5.6, packing.activeBag.estimate.excessL, 0.000001)
        assertEquals(0.0, packing.activeBag.estimate.freeL, 0.0)
        assertTrue(packing.activeBag.estimate.exceedsCapacity)
        packing.updateItem("ITEM-02") { it.copy(quantity = 2, packedQuantity = 2) }
        assertEquals(16, packing.selectedUnits)
        assertEquals(2, packing.packedUnits)
        assertEquals(38.5, packing.usedL, 0.000001)
    }

    @Test fun switchingBagsDoesNotCombineTheirCapacityOrInventory() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.updateItem("ITEM-02", LuggagePreset.Personal) { it.copy(quantity = 20) }
        packing.selectLuggage(LuggagePreset.Personal)
        assertEquals(31.5, packing.activeBag.estimate.capacityL, 0.000001)
        assertEquals(56.0, packing.usedL, 0.000001)
        assertTrue(packing.activeBag.estimate.exceedsCapacity)
        packing.selectLuggage(LuggagePreset.Cabin)
        assertEquals(48.125, packing.capacityL, 0.000001)
        assertEquals(19.2, packing.usedL, 0.000001)
        assertFalse(packing.activeBag.estimate.exceedsCapacity)
    }
}
