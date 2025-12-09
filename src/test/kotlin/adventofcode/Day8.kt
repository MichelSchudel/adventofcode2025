package adventofcode

import adventofcode.Day8.Point3D
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.Test

class Day8 {

    @Test
    fun testPart1() {
        val s = partOne("/testinput/day-8.txt")
        println(s)
    }

    @Test
    fun solvePart1() {
        val s = partOne("/realinput/day-8.txt")
        println(s)
    }

    @Test
    fun testPart2() {
        val s = partTwo("/testinput/day-8.txt")
        println(s)
    }

    @Test
    fun solvePart2() {
        val s = partTwo("/realinput/day-8.txt")
        println(s)
    }

    private data class JunctionBox(val id: Int, val position: Point3D) {
        infix fun distanceTo(other: JunctionBox): Long = position.distanceTo(other.position)
    }

    private class CircuitManager(boxPositions: Iterable<Point3D>) {

        val boxes: List<JunctionBox> = boxPositions.mapIndexed(::JunctionBox)

        private val junctionToCircuits: MutableMap<Int, Int> = mutableMapOf()

        private val circuits: List<MutableSet<Int>> = List(boxes.size) { index ->
            junctionToCircuits[index] = index
            mutableSetOf(index)
        }

        val circuitGroups: List<Set<JunctionBox>>
            get() =
                circuits.asSequence()
                    .filterNot { it.isEmpty() }
                    .sortedByDescending { it.size }
                    .map { ids -> ids.map(boxes::get).toSet() }
                    .toList()

        private val connectionCandidates: Iterator<Pair<JunctionBox, JunctionBox>> = iterator {
            while (true) {
                boxes.asSequence()
                    .flatMap { boxes.map { other -> it to other } }
                    .filterNot { it.first.id >= it.second.id }
                    .filterNot { junctionToCircuits[it.first.id] == junctionToCircuits[it.second.id] }
                    .sortedBy { (a, b) -> a distanceTo b }
                    .toList()
                    .ifEmpty { break }
                    .let { pairs -> yieldAll(pairs) }
            }
        }

        fun connectNextClosest(): Pair<JunctionBox, JunctionBox>? {
            if (!connectionCandidates.hasNext()) return null
            return connectionCandidates.next().also { (a, b) -> connect(a, b) }
        }

        private fun connect(a: JunctionBox, b: JunctionBox) {
            check(a.id < b.id) { "Internal convention broken! Junction box ids should be ordered." }

            val (circuitToGrow, circuitToShrink) = run {
                val circuitOfA = junctionToCircuits.getValue(a.id)
                val circuitOfB = junctionToCircuits.getValue(b.id)

                if (circuitOfA == circuitOfB) return

                if (circuits[circuitOfA].size >= circuits[circuitOfB].size) circuitOfA to circuitOfB
                else circuitOfB to circuitOfA
            }

            circuits[circuitToGrow].addAll(circuits[circuitToShrink])
            circuits[circuitToShrink].forEach { box -> junctionToCircuits[box] = circuitToGrow }
            circuits[circuitToShrink].clear()
        }
    }

    data class Point3D(val x: Long, val y: Long, val z: Long) {}

    private fun buildModelFromInput(input: String): CircuitManager {
        val lines = loadStringLines(input)
        return lines.map { it.split(",").let { Point3D(it[0].toLong(), it[1].toLong(), it[2].toLong()) } }.asIterable().let(::CircuitManager)
    }

    fun partOne(input: String = ""): Long = buildModelFromInput(input).run {
        repeat(times = if (boxes.size > 20) 1000 else 10) { connectNextClosest() }
        return circuitGroups.take(3).map { it.size.toLong() }.reduce(Long::times)
    }

    fun partTwo(input: String): Long = buildModelFromInput(input).run {
        var lastConnected: Pair<JunctionBox, JunctionBox>? = null
        var iterations = 0
        while (iterations++ < 10_000 && circuitGroups.size > 1) { lastConnected = connectNextClosest() }
        return lastConnected?.run { first.position.x * second.position.x } ?: error("Could not connect circuits.")
    }

}

infix fun Point3D.distanceTo(other: Point3D): Long {
    var d = 0.0
    d += (this.x - other.x).toDouble().pow(2.0)
    d += (this.y - other.y).toDouble().pow(2.0)
    d += (this.z - other.z).toDouble().pow(2.0)
    return sqrt(d).toLong()
}
