package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11 {

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/testinput/day-11.txt")
        assertThat(solve(currentNode = "you", seenDac = true, seenFft = true, model = model)).isEqualTo(5)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/realinput/day-11.txt")
        println(solve(currentNode = "you", seenDac = true, seenFft = true, model = model))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/testinput/day-11-part-2.txt")
        assertThat(solve(currentNode = "svr", seenDac = false, seenFft = false, model = model)).isEqualTo(2)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/realinput/day-11.txt")
        println(solve(currentNode = "svr", seenDac = false, seenFft = false, model = model))
    }

    fun solve(
        currentNode: String,
        seenDac: Boolean,
        seenFft: Boolean,
        model: Map<String, List<String>>,
        cache: MutableMap<Triple<String, Boolean, Boolean>, Long> = mutableMapOf()
    ): Long {
        val key = Triple(currentNode, seenDac, seenFft)
        cache[key]?.let { return it }

        return when (currentNode) {
            "out" -> {
                if (seenDac && seenFft) 1 else 0
            }
            else -> {
                model[currentNode]!!.sumOf {
                    solve(it, seenDac || (currentNode == "dac"), seenFft || (currentNode == "fft"), model, cache)
                }
            }
        }.also { cache[key] = it }
    }


    private fun buildModelFromInput(input: String): Map<String, List<String>> =
        loadStringLines(input).associate {
            it.substringBefore(":").trim() to it.substringAfter(":").trim().split(" ")
        }

}

