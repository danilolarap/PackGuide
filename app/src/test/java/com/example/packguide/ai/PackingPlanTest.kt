package com.example.packguide.ai

import com.example.packguide.ui.screens.LuggagePreset
import com.example.packguide.ui.screens.PackingState
import com.example.packguide.ui.screens.PackingZone
import org.junit.Assert.*
import org.junit.Test

class PackingPlanTest {
    @Test fun twelveSelectedButElevenPackedRequestsExactlyEleven() {
        val packing = PackingState()
        packing.updateItem("ITEM-01") { it.copy(quantity = 12, packedQuantity = 11) }
        val plan = PackingPlan.from(packing.activeBag)
        assertEquals(1, plan.contents.size)
        assertEquals("ITEM-01", plan.contents.single().id)
        assertEquals(11, plan.contents.single().quantity)
        assertTrue(plan.description.contains("11 × CAMISETAS"))
        assertFalse(plan.description.contains("12 ×"))
        assertFalse(plan.description.contains("PANTALONES"))
    }

    @Test fun uncheckedTypesAreOmittedAndEachBagsPackedInventoryIsIndependent() {
        val packing = PackingState()
        packing.toggleLuggage(LuggagePreset.Personal)
        packing.updateItem("ITEM-01", LuggagePreset.Personal) { it.copy(quantity = 12, packedQuantity = 11) }
        packing.updateItem("ITEM-02", LuggagePreset.Cabin) { it.copy(packedQuantity = 1) }
        val personal = PackingPlan.from(packing.bag(LuggagePreset.Personal))
        val cabin = PackingPlan.from(packing.bag(LuggagePreset.Cabin))
        assertEquals(listOf(45, 35, 20), personal.dimensions)
        assertEquals(listOf("ITEM-01"), personal.contents.map { it.id })
        assertEquals(listOf("ITEM-02"), cabin.contents.map { it.id })
        assertEquals(11, personal.contents.single().quantity)
        assertEquals(1, cabin.contents.single().quantity)
    }

    @Test fun checkingOrUncheckingUnitsInvalidatesTheImageButUnpackedSelectionsDoNot() {
        val packing = PackingState()
        val bag = packing.activeBag
        assertTrue(PackingPlan.from(bag).contents.isEmpty())
        bag.setUnitPacked("ITEM-01", 2, true)
        val first = PackingPlan.from(bag)
        assertEquals(1, first.contents.single().quantity)
        packing.updateItem("ITEM-01") { it.copy(quantity = 12) }
        assertEquals(first, PackingPlan.from(bag))
        bag.setUnitPacked("ITEM-01", 0, true)
        val second = PackingPlan.from(bag)
        assertNotEquals(first, second)
        assertEquals(2, second.contents.single().quantity)
        bag.setUnitPacked("ITEM-01", 2, false)
        assertEquals(first, PackingPlan.from(bag))
        bag.setUnitPacked("ITEM-01", 0, false)
        assertTrue(PackingPlan.from(bag).contents.isEmpty())
    }

    @Test fun immutableSnapshotsRetainCountsAndDetectReferenceChanges() {
        val packing = PackingState()
        packing.updateItem("ITEM-01") { it.copy(packedQuantity = 2) }
        val before = PackingPlan.from(packing.activeBag)
        packing.updateItem("ITEM-01") { it.copy(zone = PackingZone.Top) }
        assertNotEquals(before, PackingPlan.from(packing.activeBag))
        packing.heightCm.value += 1
        assertNotEquals(before, PackingPlan.from(packing.activeBag))
        assertEquals(55, before.dimensions.first())
        assertEquals(2, before.contents.single().quantity)
        assertEquals(PackingZone.Base.label, before.contents.single().zone)
    }
}
