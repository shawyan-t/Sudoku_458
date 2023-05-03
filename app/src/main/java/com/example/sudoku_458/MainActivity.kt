package com.example.sudoku_458

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import java.io.IOException

const val SAVE_FILE = "saved_games.txt"

class MainActivity : AppCompatActivity() {

    private lateinit var myGrid: MyGrid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myGrid = findViewById(R.id.myGrid)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewGame -> myGrid.newGame()
            R.id.menuSaveGame -> Toast.makeText(this,
                if (saveGame()) "Game Saved."
                else "Couldn't save game!", Toast.LENGTH_SHORT).show()
            R.id.menuLoadRecentGame -> Toast.makeText(this,
                if (loadRecentGame()) "Recent Game Loaded."
                else "Couldn't load game!", Toast.LENGTH_SHORT).show()
            R.id.menuCloseApp -> finish()
            else -> Toast.makeText(this, "Not Implemented!", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    // Saving can currently only save one game at a time.
    fun saveGame(): Boolean {
        try {
            openFileOutput(SAVE_FILE, MODE_PRIVATE).use {ostream ->
                var gstr = gameToJson(myGrid.getGame())
                ostream.writer().write(gstr)
                Log.v("Writing File", gstr)
            }
        } catch (err: IOException) {
            err.printStackTrace()
            return false
        }
        return true
    }

    fun loadRecentGame(): Boolean {
        try {
            var lines = openFileInput(SAVE_FILE).reader().readLines()
            Log.v("Reading File", lines.toString())
            if (lines.isEmpty()) return false
            myGrid.setGame(gameFromJson(lines.first()))
        } catch (err: IOException) {
            err.printStackTrace()
            return false
        }
        return true
    }
}