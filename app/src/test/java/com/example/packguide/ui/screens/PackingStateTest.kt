package com.example.packguide.ui.screens

import org.junit.Assert.*
import org.junit.Test

class PackingStateTest {
    @Test fun everyNonEmptyLuggageCombinationIsAvailable() {
        for (mask in 1..7) {
            val packing = PackingState()
            val expected = LuggagePreset.entries.filter { mask and (1 shl it.ordinal) != 0 }
            expected.filter { it !in packing.selectedLuggage }.forEach(packing::toggleLuggage)
            packing.selectedLuggage.filter { it !in expected }.forEach(packing::toggleLuggage)
            assertEquals(expected, packing.selectedLuggage)
            assertTrue(packing.activeLuggage in expected)
            assertEquals(expected.sumOf { it.capacityL }, packing.totalCapacityL, 0.001)
        }
    }

    @Test fun quantitiesPackingProgressAndDimensionsAreIndependentForEachBag() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.toggleLuggage(LuggagePreset.Checked)
        val cabinItems = packing.bag(LuggagePreset.Cabin).items.toList()
        packing.updateItem("ITEM-01", LuggagePreset.Personal) { it.copy(quantity = 2, packedQuantity = 1) }
        packing.updateItem("ITEM-01", LuggagePreset.Checked) { it.copy(quantity = 5, packedQuantity = 3, zone = PackingZone.Top) }
        packing.bag(LuggagePreset.Personal).heightCm.value = 40
        assertEquals(cabinItems, packing.bag(LuggagePreset.Cabin).items.toList())
        assertEquals(2.4, packing.bag(LuggagePreset.Personal).usedL, 0.001)
        assertEquals(6.0, packing.bag(LuggagePreset.Checked).usedL, 0.001)
        assertEquals(28.0, packing.bag(LuggagePreset.Personal).capacityL, 0.001)
        assertEquals(55, packing.bag(LuggagePreset.Cabin).heightCm.value)
        assertEquals(4, packing.totalPackedUnits)
        assertEquals(cabinItems.sumOf { it.quantity } + 7, packing.totalSelectedUnits)
        packing.selectLuggage(LuggagePreset.Checked)
        assertEquals(5, packing.selectedUnits)
        assertEquals(3, packing.packedUnits)
        assertEquals(PackingZone.Top, packing.items.first().zone)
        packing.selectLuggage(LuggagePreset.Personal)
        assertEquals(2, packing.selectedUnits)
        assertEquals(1, packing.packedUnits)
        assertEquals(PackingZone.Base, packing.items.first().zone)
    }

    @Test fun removingBagClearsOnlyItsAssignmentsAndChoosesAValidActiveBag() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.updateItem("ITEM-01", LuggagePreset.Personal) { it.copy(quantity = 2) }
        packing.toggleLuggage(LuggagePreset.Cabin)
        assertEquals(listOf(LuggagePreset.Personal), packing.selectedLuggage)
        assertEquals(LuggagePreset.Personal, packing.activeLuggage)
        assertEquals(2, packing.totalSelectedUnits)
        packing.updateItem("ITEM-01", LuggagePreset.Cabin) { it.copy(quantity = 99) }
        assertEquals(2, packing.totalSelectedUnits)
        packing.selectLuggage(LuggagePreset.Cabin)
        assertEquals(LuggagePreset.Personal, packing.activeLuggage)
        packing.toggleLuggage(LuggagePreset.Cabin)
        assertEquals(0, packing.bag(LuggagePreset.Cabin).selectedUnits)
        assertEquals(2, packing.totalSelectedUnits)
    }

    @Test fun atLeastOneBagIsRequiredAndAllSelectedMeasuresMustBeConfirmed() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Cabin)
        assertEquals(listOf(LuggagePreset.Cabin), packing.selectedLuggage)
        assertFalse(packing.allDimensionsConfirmed)
        packing.dimensionsConfirmed.value = true
        assertTrue(packing.allDimensionsConfirmed)
        packing.toggleLuggage(LuggagePreset.Checked)
        assertFalse(packing.allDimensionsConfirmed)
        packing.bag(LuggagePreset.Checked).dimensionsConfirmed.value = true
        assertTrue(packing.allDimensionsConfirmed)
        packing.toggleLuggage(LuggagePreset.Checked)
        assertTrue(packing.allDimensionsConfirmed)
    }
}
