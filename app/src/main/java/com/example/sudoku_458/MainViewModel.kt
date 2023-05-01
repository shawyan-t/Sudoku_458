package com.example.sudoku_458

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Store a shared instance of the GameModel so the grid fragment can keep its data even when it is
// swapped out for the menu fragment.
class MainViewModel(
    val dao: GameModelDao
): ViewModel() {
    /*
    private val mutableGameModel = MutableLiveData<GameModel>()
    val gameModel: LiveData<GameModel> get() = mutableGameModel

    fun setGameModel(gameModel: GameModel) {
        mutableGameModel.value = gameModel
    }
     */
    // Repository here? Seems unnecessary
    val gameModel = MutableLiveData<GameModel>()

    fun init() {

    }
}