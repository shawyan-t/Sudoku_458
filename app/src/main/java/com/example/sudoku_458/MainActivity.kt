package com.example.sudoku_458

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

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

        return true
    }

    fun loadRecentGame(): Boolean {

        return true
    }
}