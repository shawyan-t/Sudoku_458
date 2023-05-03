package com.example.sudoku_458

import com.google.gson.Gson

data class GameData(
    var grid: Array<IntArray>,
    var orig: Array<BooleanArray>,
    var time: Long
) {
    // Auto generated equals() that uses deep equality
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameData

        if (!grid.contentDeepEquals(other.grid)) return false
        if (!orig.contentEquals(other.orig)) return false

        return true
    }

    // Auto generated hashCode() that uses deep equality
    override fun hashCode(): Int {
        var result = grid.contentDeepHashCode()
        result = 31 * result + orig.contentHashCode()
        return result
    }
}

fun gameToJson(game: GameData): String {
    val gson = Gson()
    return gson.toJson(game)
}

fun gameFromJson(json: String): GameData {
    val gson = Gson()
    return gson.fromJson(json, GameData::class.java)
}
