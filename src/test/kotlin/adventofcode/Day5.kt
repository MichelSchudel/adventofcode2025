package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5 {

    data class Coordinate(val x: Int, val y: Int, val hasRoll: Boolean)


    @Test
    fun testPart1() {
        val model = buildModelFromInput("/testinput/day-5.txt")
        assertThat(solvePart1(model)).isEqualTo(3)
    }


    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/realinput/day-5.txt")
        println(solvePart1(model))
    }

    private fun solvePart1(model: Pair<List<Pair<Long, Long>>, List<Long>>):Long {
        return model.second.count { product -> model.first.any { range -> product >= range.first && product <= range.second } }
            .toLong()
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/testinput/day-5.txt")
        assertThat(countCoveredElements(model.first)).isEqualTo(14)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/realinput/day-5.txt")
        println(countCoveredElements(model.first))
    }

    fun countCoveredElements(ranges: List<Pair<Long,Long>>): Long {
        val sorted = ranges.sortedBy { it.first }

        var mergedStart = sorted[0].first
        var mergedEnd = sorted[0].second
        var total = 0L

        for (i in 1 until sorted.size) {
            val currentRange = sorted[i]

            if (currentRange.first <= mergedEnd + 1) {
                mergedEnd = maxOf(mergedEnd, currentRange.second)
            } else {
                total += (mergedEnd - mergedStart + 1)
                mergedStart = currentRange.first
                mergedEnd = currentRange.second
            }
        }

        total += (mergedEnd - mergedStart + 1)

        return total
    }

    private fun buildModelFromInput(input: String): Pair<List<Pair<Long,Long>>, List<Long>> {
        val lines = loadStringLines(input)
        val ranges = lines.takeWhile { it != "" }
        val products = lines.dropWhile { it!= "" }.drop(1)
        val r = ranges.map { it.split("-") }.map { Pair(it[0].toLong(), it[1].toLong()) }
        val p = products.map { it.toLong() }
        return Pair(r,p)
    }


}
