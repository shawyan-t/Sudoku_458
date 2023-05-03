package com.example.sudoku_458

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

// Currently Selected button
var selected = 1

// Initial game state for drawing
var restart = 1

// Game state indicator at top
var solved = 0
class MyGrid(context: Context?, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener {

    private var grWidth: Float = 0.0f
    private var grHeight: Float = 0.0f
    private var grMidWidth: Float = 0.0f
    private var grMidHeight: Float = 0.0f
    private var startTime = SystemClock.elapsedRealtime()

    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            invalidate()
            timerHandler.postDelayed(this, 1000)
        }
    }
    private var gameState = OneSudoku()
    init {
        // Start the timer
        timerHandler.post(timerRunnable)
        newGame()
    }


    /*
       Paint and Grid Size values can be adjusted easily below
       in case we want a red, blue, green, yellow, etc. themed grid
       .
       .
       .
       Or if we want to adjust the size ( Currently 9x9 )
     */

    private val colorOfLines = Paint().apply { color = Color.BLACK; strokeWidth = 1f }
    private val thickLines = Paint().apply { color = Color.BLACK; strokeWidth = 10f }
    private val gridSize = 10

    // Use newly established grid array to keep track of text we click on and want to modify
    // use this for the sudoku board
    private var grid = Array(gridSize) { IntArray(gridSize) }


    // Use this for inside the grid numbers
    private val numTextProperties = Paint().apply {
        color = Color.BLACK; textSize = (gridSize * 16f); textAlign = Paint.Align.CENTER }
    private val numGenTextProperties = Paint().apply {
        color = Color.GRAY; textSize = (gridSize * 16f); textAlign = Paint.Align.CENTER }

    // Use this for the buttons outside the grid
    private val gridButtonsSELECTED = Paint().apply {
        color = Color.RED; textSize = (gridSize * 16f); textAlign = Paint.Align.CENTER }

    private val playerTextDisplays = Paint().apply {
        color = Color.BLACK; textSize = 50f; textAlign = Paint.Align.CENTER }

    private var mDetector = GestureDetectorCompat(this.context,this)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mDetector.onTouchEvent(event)) {

            // Debug print
            //println("On touch mdetector true")
            return true
        }
        //Debug print
        //println("On touch super")
        return super.onTouchEvent(event)
    }

    /* Adjust size for screen */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        grWidth = w.toFloat()
        grHeight = h.toFloat()
        grMidWidth = grWidth / 2
        grMidHeight = grHeight / 2
    }

    fun newGame() {
        gameState.genNew()
    }

    fun getGame(): GameData {
        return GameData(gameState.getGrid(), gameState.getOgLocs())
    }

    fun setGame(game: GameData) {
        gameState.setGrid(game.grid)
        gameState.setOgLocs(game.orig)
    }

    /* Take Canvas drawing, let parent object do drawing */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
           Local Variables for onDraw calculate the standard size / boundaries
           given a standard Num X Num size grid (Num in this case being 9)
        */

        val elapsedTime = (SystemClock.elapsedRealtime() - startTime) / 1000
        val timer = "${elapsedTime}s"

        val horizontalBoundary = width.toFloat() / gridSize
        val verticalBoundary = height.toFloat() / gridSize

        var lineit = 3
        var colorVar = colorOfLines
        /* ِDisplay Vertical Grid Lines */
        for (i in 1..gridSize) {
            if ((lineit % 3) == 0) {
                colorVar = thickLines
            }
            else {
                colorVar = colorOfLines
            }
            canvas.drawLine(i * horizontalBoundary,
                192f,
                i * horizontalBoundary,
                height.toFloat(),
                colorVar
            )
            lineit = lineit+1
        }

        var lineitHori = 3
        colorVar = colorOfLines
        /* ِDisplay Horizontal Grid Lines */
        for (i in 1..gridSize) {
            if ((lineitHori % 3) == 0) {
                colorVar = thickLines
            }
            else {
                colorVar = colorOfLines
            }
            canvas.drawLine(
                110f,
                i * verticalBoundary,
                width.toFloat(),
                i * verticalBoundary,
                colorVar
            )
            lineitHori = lineitHori+1
        }

        /* Display Number within Grid Lines */
        /* Redrawn Grid from previous numberGrid project.

            This time:
            val Text is utilized to keep track of the current position with the
            grid array we want to modify.

            `verticalAxis` and `displayNumStandard` from previous project written out
             and merged into single line declarations for the x and y boundaries, respectively

             X and Y boundaries no longer utilize horizontalAdjust and VerticalAdjust, as it has
             been phased out by the removal of displayNumStandard
         */

        // Use adjusted value to get valid range of iterable indexes within grid
        val drawAdjusted = gridSize-1

        val sgrid = gameState.getGrid()
        val genVals = gameState.getOgLocs()
        if (restart != 1) {
            for (i in 0 until drawAdjusted) {
                for (j in 0 until drawAdjusted) {

                    val text = sgrid[i][j].toString()        //  ** Put Sudoku Numbers here **
                    if (text == "0") continue // Don't draw zeros
                    val props =  if (genVals[i][j])  numGenTextProperties else numTextProperties
                    // Center the X axis with the Horizontal boundary
                    // Center the Y axis and with Vertical boundary and divide by 3 to center text
                    val x = ((i + 1) * horizontalBoundary) + (horizontalBoundary / 2); val y = ((j + 1) * verticalBoundary) + (verticalBoundary / 2) + (numTextProperties.textSize / 3)
                    canvas.drawText(text,x,y,props)
                }
            }
        }


        // After restarting or the initialization, the draw will always go here unless restart is pressed
        else {
            for (i in 1..drawAdjusted) {
                for (j in 1..drawAdjusted) {
                    val text = "\u200E " // Invisible character

                    // Center the X axis with the Horizontal boundary
                    // Center the Y axis and with Vertical boundary and divide by 3 to center text

                    canvas.drawText(
                        text,
                        (i * horizontalBoundary) + (horizontalBoundary / 2),
                        (j * verticalBoundary) + (verticalBoundary / 2) + (numTextProperties.textSize / 3),
                        numTextProperties
                    )
                }
            }
        }

        for (i in 1..drawAdjusted){
            val text = i.toString()

            if (i == selected) {

                canvas.drawText(
                    text,
                    50f,
                    ((i + 0.77) * verticalBoundary).toFloat(),
                    gridButtonsSELECTED
                )
            }

            else {
                canvas.drawText(
                    text,
                    50f,
                    ((i + 0.77) * verticalBoundary).toFloat(),
                    numTextProperties
                )
            }
        }

        // Restart button randomizes a new sudoku board and resets all flags and tips and stuff
        val text = "Restart"
        canvas.drawText(
            text,
            ((1.5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )


        // This text only needs to be changed if Sudoku is solved. Game Logic checker will change text here if flag is met

        if (solved == 0) {
            val playerSolvedtext = "Unsolved"
            canvas.drawText(
                playerSolvedtext,
                ((5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
                100f,
                playerTextDisplays
            )
        }
        else {
            val playerSolvedtext = "Solved!"
            canvas.drawText(
                playerSolvedtext,
                ((5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
                100f,
                playerTextDisplays
            )
        }

        // I was too lazy to add this tonight but basically a timer needs to go here
        val timerText = timer
        canvas.drawText(
            timerText,
            ((8.23).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )



        // Start drawing grid after initialization or restart, set flag
        if (restart == 1){
            restart = 0
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timerHandler.removeCallbacks(timerRunnable)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent) {

    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        /* Calculate the event coordinates divided by the width or height
            and grid size respectively
         */

        // Tapped Coordinates
        val column = (p0.x / grWidth * gridSize).toInt()
        val row = (p0.y / grHeight * gridSize).toInt()

        if (column == 0){
            selected = row
        }

        // Restart got hit
        else if (((column == 1) and (row == 0)) or ((column == 2) and (row == 0))) {
            restart = 1
            startTime = SystemClock.elapsedRealtime()
            newGame()
            for (row in grid.indices) {
                grid[row].fill(0)
            }
        }
        else { // Normal grid tap
            val ogs = gameState.getOgLocs()
            if (!ogs[column - 1][row - 1]) { // Only allow clicking if not a generated value
                gameState.setVal(column - 1, row - 1, selected)
                if (gameState.isSolved()) {
                    solved = 1
                }
            }
        }

        /* Redraw grid */

        invalidate()
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {

    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }
}


