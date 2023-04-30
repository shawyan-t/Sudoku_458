package com.example.sudoku_458

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val gridFragment = GridFragment()
        val menuFragment = MenuFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentBody, menuFragment)
            commit()
        }

        val resumeButton: Button = findViewById(R.id.resumeGame)
        val menuButton: Button = findViewById(R.id.openMenu)

        resumeButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentBody, gridFragment)
                addToBackStack(null)
                commit()
            }
        }

        menuButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentBody, menuFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

}