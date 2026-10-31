package com.example.packguide.scanning

data class PackingEstimate(val capacityL: Double, val usableL: Double, val usedL: Double) {
    val exceedsAllowance: Boolean get() = usedL > usableL
}

object PackingRules {
    fun estimate(height: Int, width: Int, depth: Int, volumes: List<Pair<Int, Double>>): PackingEstimate {
        require(listOf(height, width, depth).all { it in 1..300 })
        require(volumes.all { (count, volume) -> count >= 0 && volume.isFinite() && volume >= 0 })
        val capacity = height.toDouble() * width * depth / 1000.0
        return PackingEstimate(capacity, capacity * 0.8, volumes.sumOf { (count, volume) -> count * volume })
    }
}
