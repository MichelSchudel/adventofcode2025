package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Day10 {


    @Test
    fun testPart1() {
        assertThat(solve(buildModelFromInput("/testinput/day-10.txt"))).isEqualTo(7)
    }

    @Test
    fun solvePart1() {
        println(solve(buildModelFromInput("/realinput/day-10.txt")))
    }

    @Test
    fun testPart2() {
        //assertThat(solve(buildModelFromInput("/testinput/day-10.txt"), useGreenTiles = true).size).isEqualTo(24)
    }

    @Test
    fun solvePart2() {
        ///println(solve(buildModelFromInput("/realinput/day-10.txt"), useGreenTiles = true).size)
    }

    private fun solve(model: List<Pair<Int, List<Int>>>): Int {
        return model.map { shortestXorSequence(it.first, it.second) }.sum()
    }

    fun shortestXorSequence(target: Int, operands: List<Int>): Int {
        if (target == 0) throw IllegalArgumentException("Target cannot be 0")

        // BFS queue: holds the current value
        val queue = ArrayDeque<Int>()
        queue.add(0)

        // keep track of how we got to each value
        val parent = mutableMapOf<Int, Pair<Int, Int>>()
        // value -> (previousValue, operandUsed)

        val visited = mutableSetOf<Int>()
        visited.add(0)

        while (queue.isNotEmpty()) {
            val value = queue.removeFirst()

            for (op in operands) {
                val next = value xor op
                if (next !in visited) {
                    visited.add(next)
                    parent[next] = value to op

                    if (next == target) {
                        // reconstruct path of operands
                        val ops = mutableListOf<Int>()
                        var cur = next
                        while (cur != 0) {
                            val (prev, usedOp) = parent[cur]!!
                            ops.add(usedOp)
                            cur = prev
                        }
                        ops.reverse()
                        return ops.size
                    }
                    queue.add(next)
                }
            }
        }

        // unreachable
        throw IllegalStateException("No path found")
    }

    private fun buildModelFromInput(input: String): List<Pair<Int, List<Int>>> {
        val lines = loadStringLines(input)

        val model = lines.map { mapToTuples(it) }
        return model
    }

     private fun mapToTuples(line: String): Pair<Int, List<Int>> {
         val mainRegex = Regex("""^\[(.*?)\]\s*((?:\([^()]*\)\s*)+)(?=\s*\{)""")
         val tupleRegex = Regex("""\(([^()]+)\)""")

         val match = mainRegex.find(line)
         if (match != null) {
             val header = match.groupValues[1].reversed()
             val tuplesText = match.groupValues[2]

             val tuples = tupleRegex.findAll(tuplesText).map { it.groupValues[1] }.toList()
             return Pair(convertHeaderToInt(header), tuples.map{convertButtonsToInts(it)})
         }
         throw IllegalArgumentException("Invalid input: $line")
     }
    private fun convertHeaderToInt(header:String):Int {
        val value = header.reversed()
            .mapIndexed { bit, c -> if (c == '#') 1 shl bit else 0 }
            .sum()
        return value
    }

    private fun convertButtonsToInts(tuples:String):Int {
        val bits =tuples.split(",").map { it.toInt() }
        val value = bits.sumOf { 1 shl it }
        return value
    }


}

