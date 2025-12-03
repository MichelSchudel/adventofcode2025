package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3 {
    @Test
    fun testPart1() {
        val list = buildModelFromInput("/testinput/day-3.txt")
        assertThat(solve(list, 2)).isEqualTo(357)
    }

    @Test
    fun solvePart1() {
        val list = buildModelFromInput("/realinput/day-3.txt")
        println(solve(list, 2))
    }

    @Test
    fun testPart2() {
        val list = buildModelFromInput("/testinput/day-3.txt")
        assertThat(solve(list, 12)).isEqualTo(3121910778619)
    }

    @Test
    fun solve() {
        val list = buildModelFromInput("/realinput/day-3.txt")
        println(solve(list, 12))
    }

    private fun solve(input: List<String>, neededElements: Int): Long =
        input.map { line -> findJoltage(line.map { it.toString().toInt() }, neededElements) }
            .sumOf { it.toLong() }

    private fun findJoltage(elements: List<Int>, neededElements: Int): String {
        var currentList = elements
        var n = neededElements
        var joltage = ""
        do {
            val currentLargestElementPlusIndex = currentList
                .dropLast(n - 1)
                .foldIndexed(Pair(0,0), {index, acc, e -> if (e > acc.first) Pair(e, index) else acc})
            joltage += currentLargestElementPlusIndex.first.toString()
            currentList = currentList.drop(currentLargestElementPlusIndex.second + 1)
            n--
        } while (n > 0)
        return joltage
    }

    private fun buildModelFromInput(input: String): List<String> {
        val lines = loadStringLines(input)
        return lines
    }


}
