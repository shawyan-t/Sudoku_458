package com.example.sudoku_458

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

// Data Access Object
@Dao
interface GameModelDao {
    @Upsert
    fun saveGame(gameModel: GameModel)

    @Delete
    fun deleteGame(gameModel: GameModel)

    @Query("SELECT * FROM game ORDER BY id ASC")
    fun getSavedGames(): List<GameModel>
}