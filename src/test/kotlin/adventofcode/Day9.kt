package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Day9 {


    @Test
    fun testPart1() {
        assertThat(solve(buildModelFromInput("/testinput/day-9.txt"), useGreenTiles = false).size).isEqualTo(50)
    }

    @Test
    fun solvePart1() {
        println(solve(buildModelFromInput("/realinput/day-9.txt"), useGreenTiles = false).size)
    }

    @Test
    fun testPart2() {
        assertThat(solve(buildModelFromInput("/testinput/day-9.txt"), useGreenTiles = true).size).isEqualTo(24)
    }

    @Test
    fun solvePart2() {
        println(solve(buildModelFromInput("/realinput/day-9.txt"), useGreenTiles = true).size)
    }

    data class Edge(val start: Point, val end: Point) {

        val area: Area = Area(start, end)
    }

    fun <T : Any> Iterable<T>.distinctPairs(): Sequence<Pair<T, T>> = sequence {
        val iter = this@distinctPairs.iterator()
        if (!iter.hasNext()) return@sequence
        val previous = mutableListOf(iter.next())
        while(iter.hasNext()) {
            val second = iter.next()
            for (first in previous) yield(first to second)
            previous.add(second)
        }
    }
    data class Area(val xRange: LongRange, val yRange: LongRange) : Iterable<Point> {

        constructor(p1: Point, p2: Point) : this(
            xRange = min(p1.x, p2.x)..max(p1.x, p2.x),
            yRange = min(p1.y, p2.y)..max(p1.y, p2.y),
        )

        operator fun contains(point: Point) = point.x in xRange && point.y in yRange

        override fun iterator(): Iterator<Point> = iterator {
            for (x in xRange) {
                for (y in yRange) {
                    yield(Point(x, y))
                }
            }
        }
    }

    infix fun Area.overlaps(other: Area): Boolean {
        val horizontalOverlap = maxOf(xRange.first, other.xRange.first) <= minOf(xRange.last, other.xRange.last)
        val verticalOverlap = maxOf(yRange.first, other.yRange.first) <= minOf(yRange.last, other.yRange.last)
        return horizontalOverlap && verticalOverlap
    }

    val Area.size: Long
        get() =
            (xRange.last - xRange.first).absoluteValue.inc() * (yRange.last - yRange.first).absoluteValue.inc()

    data class Point(val x: Long, val y: Long)

    private fun List<Point>.toRectangles(): List<Area> {
        if (size < 2) return emptyList()
        return this
            .distinctPairs()
            .map { (a, b) -> Area(a, b) }
            .sortedByDescending { it.size }
            .toList()
    }

    private fun List<Point>.toEdges(): Set<Edge> {
        if (size < 2) return emptySet()
        return this
            .plusElement(this.first())
            .zipWithNext { a, b -> Edge(a, b) }
            .toSet()
    }

    private fun Area.isValid(edges: Set<Edge>): Boolean {
        val inside = Area(
            xRange = (xRange.first + 1)..(xRange.last - 1),
            yRange = (yRange.first + 1)..(yRange.last - 1),
        )
        return edges.none { it.area overlaps inside }
    }

    private fun solve(redTiles: List<Point>, useGreenTiles: Boolean): Area {
        val edges = if(useGreenTiles) redTiles.toEdges() else emptySet()
        return redTiles
            .toRectangles()
            .filter { it.isValid(edges) }
            .maxBy { it.size }
    }

    private fun buildModelFromInput(input: String): List<Point> {
        val lines = loadStringLines(input)
        val r = lines.map { it.split(",") }.map { Point(it[0].toLong(), it[1].toLong()) }
        return r
    }

}

