package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day2 {
    @Test
    fun testPart1() {
        val list = buildModelFromInput("/testinput/day-2.txt")
        assertThat(solvePart1(list)).isEqualTo(1227775554L)
    }

    @Test
    fun solvePart1() {
        val list = buildModelFromInput("/realinput/day-2.txt")
        println(solvePart1(list))
    }

    @Test
    fun testPart2() {
        val list = buildModelFromInput("/testinput/day-2.txt")
        assertThat(solvePart2(list)).isEqualTo(4174379265L)
    }

    @Test
    fun solvePart2() {
        val list = buildModelFromInput("/realinput/day-2.txt")
        println(solvePart2(list))
    }

    private fun solvePart1(input: List<Pair<Long,Long>>): Long {
       //
        val regexp = Regex("^(\\d+)\\1\$")
        return input
                .flatMap { (start, end) -> (start..end).asSequence() }
                .filter { element -> regexp.matches  (element.toString()) }
                .sum()

    }

    private fun solvePart2(input: List<Pair<Long,Long>>): Long {
        //
        val regexp = Regex("^(\\d+)\\1+\$")
        return input
            .flatMap { (start, end) -> (start..end).asSequence() }
            .filter { element -> regexp.matches  (element.toString()) }
            .sum()
    }

    private fun buildModelFromInput(input: String): List<Pair<Long,Long>> {
        val lines = loadStringLines(input)
        val tokens = lines.first().split(",").map { it.split("-") }.map { Pair(it[0].toLong(), it[1].toLong()) }
        return tokens
    }


}
