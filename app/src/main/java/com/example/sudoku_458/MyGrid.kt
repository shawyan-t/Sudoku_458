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
        //timerHandler.post(timerRunnable)
        newGame()
    }


    private val colorOfLines = Paint().apply { color = Color.BLACK; strokeWidth = 1f }
    private val thickLines = Paint().apply { color = Color.BLACK; strokeWidth = 10f }
    private val gridSize = 10

    // Use newly established grid array to keep track of text we click on and want to modify
    // use this for the sudoku board

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

    private val playerTextWin = Paint().apply {
        color = Color.GREEN; textSize = 50f; textAlign = Paint.Align.CENTER }

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
        timerHandler.post(timerRunnable)
        gameState.genNew()
        startTime = SystemClock.elapsedRealtime()
        solved = 0
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

        var colorVar: Paint
        /* ِDisplay Vertical Grid Lines */
        for (i in 1..gridSize) {
            colorVar = if (((i - 1) % 3) == 0) thickLines else colorOfLines
            canvas.drawLine(i * horizontalBoundary,
                192f,
                i * horizontalBoundary,
                height.toFloat(),
                colorVar
            )
        }

        /* ِDisplay Horizontal Grid Lines */
        for (i in 1..gridSize) {
            colorVar = if (((i - 1) % 3) == 0) thickLines else colorOfLines
            canvas.drawLine(
                110f,
                i * verticalBoundary,
                width.toFloat(),
                i * verticalBoundary,
                colorVar
            )
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

        for (i in 0..drawAdjusted){
            var text = i.toString()
            if (i == 0) text = "X"
            colorVar = if (i == selected) gridButtonsSELECTED else numTextProperties

            canvas.drawText(
                text,
                50f,
                ((i + 0.77) * verticalBoundary).toFloat(),
                colorVar
            )

        }

        // Clear all button, clears all the inputted values on the current board. Leaves generated values
        canvas.drawText(
            "Clear All",
            ((1.56).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )


        // This text only needs to be changed if Sudoku is solved. Game Logic checker will change text here if flag is met
        val playerSolvedtext = if (solved == 0) "Unsolved" else "Solved"
        colorVar = if (solved == 0) playerTextDisplays else playerTextWin
        canvas.drawText(
            playerSolvedtext,
            ((5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            colorVar
        )

        // Timer text
        canvas.drawText(
            timer,
            ((8.23).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )

    }

    // Handle click event.
    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        /* Calculate the event coordinates divided by the width or height
            and grid size respectively
         */

        if (solved == 1) return true  // instakill , no input allowed if solved
        // Tapped Coordinates
        val column = (p0.x / grWidth * gridSize).toInt()
        val row = (p0.y / grHeight * gridSize).toInt()
        //println(row)
        //println(column)
        if (column == 0){
            selected = row
        }
        else if ((row == 0) and ((column == 1) or (column == 2))) { // Clear all was hit
            gameState.clearInputted()
        }
        else if ((row > 0) and (column > 0)) {                      // Normal grid tap
            val ogs = gameState.getOgLocs()
            if (!ogs[column - 1][row - 1]) {                        // Only allow clicking if not a generated value
                gameState.setVal(column - 1, row - 1, selected)
                if (gameState.isSolved()) {
                    timerHandler.removeCallbacks(timerRunnable)     // Set Pause timer
                    solved = 1                                      // Set win flag

                }
            }
        }

        /* Redraw grid */
        invalidate()
        return true
    }

    // Random required overrides for motion events, leave as is
    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {

    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
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
}


