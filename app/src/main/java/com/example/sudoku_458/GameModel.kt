package com.example.sudoku_458

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson


// Constant: Always 9 columns and rows in a game of Sudoku. TODO: Does this belong here?
const val GRID_SIZE = 9

@Entity(tableName = "game")
data class GameModel(
    // This class holds all the state necessary to track a single game. An instance of it is held
    // by the outer activity to stay persistent when individual Fragments are created and destroyed,
    // like when switching to main menu or back to grid view. Also, this is the data that is saved
    // and loaded when a user elects to save/load game.

    // Primary key for storage to database
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Total cumulative gameplay time on this game.
    var time: Int = 0,

    // The game board. 0 is the "Empty square" state.
    val grid: GameArray = GameArray()

    /* TODO: Should drawing state, such as which row/col is currently selected, and which number is
        selected in the number picker, be stored and persist across opening/closing the main menu,
        and/or across saving & loading a saved game?
        It might make sense to create a GridModel or something class instead if this is desired
    */
)

class GameArray {
    val grid: Array<IntArray> = Array(GRID_SIZE) { IntArray(GRID_SIZE) {0} }
}

class Converters {
    @TypeConverter
    fun fromArray(a: GameArray): String {
        val gson = Gson()
        return gson.toJson(a)
    }

    @TypeConverter
    fun toArray(s: String): GameArray {
        val gson = Gson()
        return gson.fromJson(s, GameArray::class.java)
    }
}