package adventofcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day4 {

    data class Coordinate(val x: Int, val y: Int, val hasRoll: Boolean)


    @Test
    fun testPart1() {
        val list = buildModelFromInput("/testinput/day-4.txt")
        assertThat(pickableRoles(list).size).isEqualTo(13)
    }

    @Test
    fun solvePart1() {
        val list = buildModelFromInput("/realinput/day-4.txt")
        println(pickableRoles(list).size)
    }

    @Test
    fun testPart2() {
        val list = buildModelFromInput("/testinput/day-4.txt")
        assertThat(solvePart2(list)).isEqualTo(43)
    }

    @Test
    fun solvePart2() {
        val list = buildModelFromInput("/realinput/day-4.txt")
        println(solvePart2(list))
    }

    fun solvePart2(coordinates: List<Coordinate>): Long {
        var list = coordinates
        var pickableRoles = pickableRoles(list)
        var pickedRoles = 0
        while (pickableRoles.isNotEmpty()) {
            list = list.filter { !pickableRoles.contains(it) }
            pickedRoles += pickableRoles.size
            pickableRoles = pickableRoles(list)
        }
        return pickedRoles.toLong()
    }

    private fun pickableRoles(coordinates: List<Coordinate>): List<Coordinate> {
        return coordinates.filter { it.hasRoll } .filter { getAdjacentRolls(coordinates, it).size <  4 }
    }

    private fun getAdjacentRolls(
        coordinates: List<Coordinate>,
        coordinate: Coordinate
    ): List<Coordinate> {
        return coordinates.filter { isAdjacentRoll(coordinate = coordinate, neighbour = it) }
    }

    private fun isAdjacentRoll(coordinate: Coordinate, neighbour: Coordinate): Boolean {
        if (abs(coordinate.x - neighbour.x) <= 1
            && abs(coordinate.y - neighbour.y) <= 1
            && coordinate != neighbour
            && neighbour.hasRoll) {
            return true
        }
        return false
    }

    private fun buildModelFromInput(input: String): List<Coordinate> {
        val lines = loadStringLines(input)
        val coordinates = lines
            .mapIndexed { y, line -> line.mapIndexed{ x, element -> Coordinate(x=x, y=y, hasRoll = element == '@')
                }
            }
        return coordinates.flatten()
    }


}
