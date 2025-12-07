package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day6 {

    data class Operand(val x: Int, val y: Int, val operand: Long)
    data class Operator(val x: Int, val y: Int, val operator: String)


    @Test
    fun testPart1() {
        val model = buildModelFromInput("/testinput/day-6.txt")
        assertThat(solvePart1(model)).isEqualTo(4277556)
    }


    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/realinput/day-6.txt")
        println(solvePart1(model))
    }

    private fun solvePart1(model: Pair<Map<Int, List<Operand>>, Map<Int, String>>):Long {
        return model.first.map { calculate(it.value.map { it.operand }, operator = model.second[it.key]!!) }.sum()
    }

    private fun calculate(operands: List<Long>, operator: String): Long = when (operator) {
        "*" -> {
            operands.map { it }.reduce { a, b -> a * b }
        }
        "+" -> {
            operands.map { it }.reduce { a, b -> a + b }
        }
        else -> {
            throw UnsupportedOperationException()
        }
    }
    @Test
    fun testPart2() {
        val model = buildModelFromInputPart2("/testinput/day-6.txt")
        assertThat(solve(model)).isEqualTo(3263827)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInputPart2("/realinput/day-6.txt")
        println(solve(model))
    }

    fun solve(input: List<String>): Long {
        val lists = splitOnBlankLine(input)
        return lists.sumOf { calculate2(it) }
    }

    fun calculate2(input: List<String>): Long {
        val operator = input.first().last()
        val operands = input.map { it.takeWhile { it.isDigit() }.trim() }.map { it.toLong() }
        return calculate(operands,operator.toString())
    }
    fun splitOnBlankLine(list: List<String>): List<List<String>> =
        list.fold(mutableListOf(mutableListOf<String>())) { acc, item ->
            if (item.isBlank()) {
                if (acc.last().isNotEmpty()) acc.add(mutableListOf())
            } else {
                acc.last().add(item)
            }
            acc
        }.filter { it.isNotEmpty() }


    private fun buildModelFromInput(input: String): Pair<Map<Int, List<Operand>>, Map<Int, String>> {
        val lines = loadStringLines(input)
        val operators = lines.takeLast(1).first().trim()
        val operands = lines.dropLast(1)
        val operandMap = operands.map { it.trim() }
            .mapIndexed { y, e-> e.split(Regex("\\s+"))
                .mapIndexed { x, e -> Operand(x =x, y=y, operand = e.toLong()) } }
            .flatten()
            .groupBy { it.x }
        val operatorMap = operators.split(Regex("\\s+")).mapIndexed { x,e -> Operator(x = x, y= 0, operator = e) }.associateBy(keySelector = {it.x}, valueTransform = {it.operator})
        return Pair(operandMap, operatorMap)
    }

    private fun buildModelFromInputPart2(input: String): List<String> {
        val lines = loadStringLines(input)
        val arrayWidth = lines.maxOf { it.length }
        val arrayHeight = lines.size
        val array = Array(arrayWidth) { Array(arrayHeight) { "" } }
        lines.forEachIndexed { y, line -> line.forEachIndexed { x, c -> array[x][y] = c.toString()  } }
        return array.map { it.joinToString("").trim() }
    }


}
