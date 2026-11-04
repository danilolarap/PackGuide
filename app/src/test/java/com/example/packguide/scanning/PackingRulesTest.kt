package com.example.packguide.scanning

import org.junit.Assert.*
import org.junit.Test

class PackingRulesTest {
    @Test fun fullInteriorCapacityAndAllSelectedItemsUseTheSameRule() {
        val estimate = PackingRules.estimate(50, 40, 20, listOf(3 to 1.2, 2 to 4.5))
        assertEquals(40.0, estimate.capacityL, 0.001)
        assertEquals(12.6, estimate.usedL, 0.001)
        assertEquals(27.4, estimate.freeL, 0.001)
        assertEquals(0.0, estimate.excessL, 0.001)
        assertFalse(estimate.exceedsCapacity)
        assertTrue(PackingRules.estimate(10, 10, 10, listOf(1 to 1.2)).exceedsCapacity)
    }

    @Test fun zeroItemsAndLargestAllowedDimensionsRemainFinite() {
        val estimate = PackingRules.estimate(300, 300, 300, emptyList())
        assertEquals(27000.0, estimate.capacityL, 0.001)
        assertEquals(0.0, estimate.usedL, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidDimensionsAreRejected() { PackingRules.estimate(0, 40, 20, emptyList()) }

    @Test fun exactFitAndSmallExcessAreComparedWithoutPrematureRounding() {
        assertFalse(PackingRules.estimate(55, 35, 25, listOf(1 to 48.125)).exceedsCapacity)
        val excess = PackingRules.estimate(55, 35, 25, listOf(1 to 48.126))
        assertTrue(excess.exceedsCapacity)
        assertEquals(0.001, excess.excessL, 0.00000001)
        assertEquals(0.0, excess.freeL, 0.0)
    }

    @Test fun invalidInventoryAndProgressAreRejected() {
        listOf(-1 to 1.0, 1 to -1.0, 1 to Double.NaN, 1 to Double.POSITIVE_INFINITY).forEach {
            assertThrows(IllegalArgumentException::class.java) { PackingRules.totalVolumeL(listOf(it)) }
        }
        assertEquals(0, PackingRules.progressPercent(0, 0))
        assertEquals(100, PackingRules.progressPercent(Int.MAX_VALUE, Int.MAX_VALUE))
        assertThrows(IllegalArgumentException::class.java) { PackingRules.progressPercent(3, 2) }
    }
}
