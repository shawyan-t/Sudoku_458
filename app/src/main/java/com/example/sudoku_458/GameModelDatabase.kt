package com.example.sudoku_458

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [GameModel::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GameModelDatabase: RoomDatabase() {
    abstract val dao: GameModelDao
}