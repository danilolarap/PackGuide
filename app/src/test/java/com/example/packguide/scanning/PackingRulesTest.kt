package com.example.packguide.scanning

import org.junit.Assert.*
import org.junit.Test

class PackingRulesTest {
    @Test fun capacityReservesTwentyPercentAndCountsAllItems() {
        val estimate = PackingRules.estimate(50, 40, 20, listOf(3 to 1.2, 2 to 4.5))
        assertEquals(40.0, estimate.capacityL, 0.001)
        assertEquals(32.0, estimate.usableL, 0.001)
        assertEquals(12.6, estimate.usedL, 0.001)
        assertFalse(estimate.exceedsAllowance)
        assertTrue(PackingRules.estimate(10, 10, 10, listOf(1 to 1.2)).exceedsAllowance)
    }

    @Test fun zeroItemsAndLargestAllowedDimensionsRemainFinite() {
        val estimate = PackingRules.estimate(300, 300, 300, emptyList())
        assertEquals(27000.0, estimate.capacityL, 0.001)
        assertEquals(0.0, estimate.usedL, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidDimensionsAreRejected() { PackingRules.estimate(0, 40, 20, emptyList()) }
}
