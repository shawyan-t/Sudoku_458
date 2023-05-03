package com.example.sudoku_458

import com.typicaldev.sudoku.Level
import com.typicaldev.sudoku.Sudoku

// Handles everything related to the sudoku game, functions include creating, checking if solved, etc
class OneSudoku() {

    private var grid = Array(9) { IntArray(9)} // Create a 9x9 grid
    private var ogLocations = Array(9) { BooleanArray(9)} // An array that has the original locations of the generated grid
    // Used to make sure users don't clear over generated values
    fun getGrid() : Array<IntArray> {
        return grid
    }
    fun getOgLocs(): Array<BooleanArray> {
        return ogLocations
    }

    // Set a value using this, make sure col & row are between 0 & 8
    fun setVal(col: Int, row: Int, value: Int) {
        grid[col][row] = value
    }

    // Creates a new random sudoku board
    fun genNew() {
        val built = Sudoku.Builder().setLevel(Level.JUNIOR).build()
        grid = built.grid
        ogLocations = Array(9) { BooleanArray(9)}
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] > 0) {
                    ogLocations[i][j] = true
                }
            }
        }
    }

    // Checks whether or not the current grid is solved or not
    fun isSolved() : Boolean {
        for (i in 0 until 9) {
            val res1 = validCol(i, grid)
            val res2 = validRow(i, grid)
            if (!res1 || !res2) return false
        }
        return validSquares(grid) && allInRange(grid)

    }

    // Loops through every value in the grid, making sure that it is between 1 and 9
    private fun allInRange(grid: Array<IntArray>) : Boolean {
        val validRange = (1..9)
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (!validRange.contains(grid[i][j])) {
                    return false
                }
            }
        }
        return true
    }

    // Makes sure that the given column has no duplicate values
    private fun validCol(col: Int, grid: Array<IntArray>) : Boolean {
        val colVal = grid[col]
        return !hasDups(colVal)
    }

    // Makes sure that the given row has no duplicates
    private fun validRow(row: Int, grid: Array<IntArray>) : Boolean {
        val rowVal = IntArray(9)
        for (i in 0 until 9) {
            rowVal[i] = (grid[i][row])
        }
        return !hasDups(rowVal)
    }

    // Loops through grid, creating arrays of all the subset squares, and makes sure they don't have duplicates
    private fun validSquares(grid: Array<IntArray>) : Boolean {
        for (col in 0 until 9 step 3) {
            for (row in 0 until 9 step 3) {
                var arr = IntArray(0).toMutableList()
                for (i in col until col+3) {
                    for (j in row until row + 3) {
                        arr.add(grid[i][j])
                    }
                }
                if (hasDups(arr.toTypedArray().toIntArray())) {
                    return false
                }
            }
        }
        return true
    }

    // Very simple array duplicate check
    private fun hasDups(arr: IntArray) : Boolean {
        return arr.size != arr.distinct().count();
    }


}