package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day1 {
    @Test
    fun testPart1() {
        val list = buildModelFromInput("/testinput/day-1.txt")
        assertThat(solvePart1(list)).isEqualTo(3)
    }

    @Test
    fun solvePart1() {
        val list = buildModelFromInput("/realinput/day-1.txt")
        println(solvePart1(list))
    }

    @Test
    fun testPart2() {
        val list = buildModelFromInput("/testinput/day-1.txt")
        assertThat(solvePart2(list)).isEqualTo(6)
    }

    @Test
    fun solvePart2() {
        val list = buildModelFromInput("/realinput/day-1.txt")
        println(solvePart2(list))
    }

    private fun solvePart1(input: List<Int>): Int {
        var passwordCounter = 0
        var dial = 50
        input.forEach {
            dial = dial + it
            if (dial.mod(100) == 0) passwordCounter++
        }
        return passwordCounter
    }

    private fun solvePart2(input: List<Int>): Int {
        var passwordCounter = 0
        var dial = 50
        input.forEach {
            repeat(abs(it)) { _->
                if (it < 0) dial = dial - 1 else dial = dial + 1
                if (dial.mod(100) == 0) passwordCounter++
            }
        }
        return passwordCounter
    }

    private fun buildModelFromInput(input: String): List<Int> {
        val lines = loadStringLines(input)
        val bothLists = buildModel(lines)
        return bothLists
    }

    private fun buildModel(lines: List<String>): List<Int> =
        lines.map { line ->
            line.drop(1).toInt() * if (line.startsWith("R")) 1 else -1
        }
}
