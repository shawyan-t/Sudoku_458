package com.example.sudoku_458

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewGame -> Toast.makeText(this, "New Game...", Toast.LENGTH_SHORT).show()
            R.id.menuCloseApp -> finish()
            else -> Toast.makeText(this, "Not Implemented!", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}