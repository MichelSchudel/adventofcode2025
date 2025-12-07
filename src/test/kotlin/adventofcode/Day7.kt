package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day7 {

    data class Splitter(val e: Char, val index: Int)
    @Test
    fun testPart1() {
        val model = buildModelFromInput("/testinput/day-7.txt")
        assertThat(solve(model).first).isEqualTo(21)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/realinput/day-7.txt")
        println(solve(model).first)
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/testinput/day-7.txt")
        assertThat(solve(model).second).isEqualTo(40)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/realinput/day-7.txt")
        println(solve(model).second)
    }

    data class Tachyon(val element: Int, val count: Long)

    private fun solve(model: Pair<List<Int>, List<String>>): Pair<Long,Long> {
        var tachyons = listOf(Tachyon(model.first.first(),1))
        val splitters = model.second
        var splitCounter = 0L
        splitters.forEach { currentLine ->
            val currentSplitters = currentLine.mapIndexed { i, e -> Splitter(e, i) }.filter { it.e == '^' }.map { it.index }
            tachyons = tachyons.map {
                if (currentSplitters.contains(it.element)) listOf(
                    Tachyon(it.element - 1, it.count),
                    Tachyon(it.element + 1, it.count)
                ).also { splitCounter++  } else {
                    listOf(it)
                }
            }.flatten()
             .groupingBy { it.element }
             .fold(0L) { acc, tachyon -> acc + tachyon.count }
             .map { (element, totalCount) -> Tachyon(element, totalCount) }
        }
        return Pair(splitCounter, tachyons.sumOf { it.count })
    }

    private fun buildModelFromInput(input: String): Pair<List<Int>, List<String>> {
        val lines = loadStringLines(input)
        val startingTachyon = lines.first().indexOfFirst { it == 'S' }
        val splitters = lines.drop(1)
        return Pair(listOf(startingTachyon), splitters)
    }

}
