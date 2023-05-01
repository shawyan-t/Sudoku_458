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
import com.typicaldev.sudoku.Sudoku

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
    private var gameState = oneSudoku()
    init {
        // Start the timer
        timerHandler.post(timerRunnable)
        gameState.genNew(35)
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

        //println(restart)
        val sgrid = gameState.getGrid()
        println(sgrid.contentDeepToString())
        if (restart != 1) {
            for (i in 0 until drawAdjusted) {
                for (j in 0 until drawAdjusted) {


                    val text = sgrid[i][j].toString()        //  ** Put Sudoku Numbers here **
                    // Center the X axis with the Horizontal boundary
                    // Center the Y axis and with Vertical boundary and divide by 3 to center text
                    canvas.drawText(
                        text,
                        ((i + 1) * horizontalBoundary) + (horizontalBoundary / 2),
                        ((j + 1) * verticalBoundary) + (verticalBoundary / 2) + (numTextProperties.textSize / 3),
                        numTextProperties
                    )
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
            for (row in grid.indices) {
                grid[row].fill(0)
            }
        }
        else { // Normal grid tap
            grid[column][row] = selected
        }

        /* Use Modulo operation to increment grid values by 1 up to 9
            resets to 0 upon 10 clicks
         */

        //grid[column][row] = (grid[column][row] + 1) % 10

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

// Handles everything related to the sudoku game, functions include creating, checking if solved, etc
class oneSudoku() {

    private var grid = Array(9) { IntArray(9)} // Create a 9x9 grid

    fun getGrid() : Array<IntArray> {
        return grid
    }

    fun setVal(col: Int, row: Int, value: Int) {
        grid[col][row] = value
    }

    // Creates a new random sudoku board, max is the number of numbers you want to get (you may get more)
    fun genNew(max: Int) {
          val NewGame = Sudoku.Builder().build()

//        val arr = genFull()
//        println(arr.contentDeepToString())
            grid = NewGame.grid
    }

    // Creates a new full solved board.
    private fun genFull() : Array<IntArray> {
        val newgrid = Array(9) { IntArray(9)}
        var tuples = genTuples().toMutableList()
        tuples.shuffle()

        val bottom = 1; val top = 9
        while (tuples.isNotEmpty()) {
            val testVal = (bottom..top).random()
            val tuple = tuples[0]
            val x = tuple[0]; val y = tuple[1]
            newgrid[x][y] = testVal
            val copy = deepCopy(newgrid)
            val solutions = solCount(0, 0, copy, 0)
            if (solutions > 0) {
                tuples.removeAt(0)
                continue
            }
            println("no solutions")
            newgrid[x][y] = 0
            tuples.shuffle()
        }
        return newgrid

    }
    private fun deepCopy(arr: Array<IntArray>) : Array<IntArray> {
        val len = arr.size
        val newarr = Array(len) { IntArray(len)}
        for (i in 0 until len) {
            for (j in 0 until len) {
                newarr[i][j] = arr[i][j]
            }
        }
        return arr
    }

    private fun genTuples(): Array<Array<Int>> {
        val arr = arrayOf<Array<Int>>().toMutableList()
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val newVal = arrayOf(i, j)
                arr.add(newVal)

            }
        }
        return arr.toTypedArray()

    }


    // solCount, returns 0 if no solutions possible, 1 if unique, 2 if not unique
    private fun solCount(oldi: Int, oldj: Int, cells: Array<IntArray>, oldcount: Int): Int {
        var i = oldi; var j = oldj; var count = oldcount
        if (j == 9) {
            j = 0;
            if (++i == 9)
                return 1 + count;
        }

        if (cells[i][j] != 0) // skip filled cells
            return solCount(i, j + 1, cells, count);
        // search for 2 solutions instead of 1
        // break, if 2 solutions are found
        for (k in 1 .. 9) {
            if (count > 1) {
                break
            }
            if (legal(i, j, k, cells)) {
                cells[i][j] = k
                // add additional solutions
                count = solCount(i, j + 1, cells, count);
            } else {
                cells[i][j] = 0; // reset on backtrack
            }
        }
        return count
    }

    // Checks whether a value can fit in a specific spot in the given sudoku grid
    private fun legal(row: Int, col: Int, value: Int, cells: Array<IntArray>): Boolean {
        // Check row fitting
        for (el in cells[row]) {
            if (el == value) return false
        }
        // Check column fitting
        for (el in cells) {
            if (el[col] == value) return false
        }
        // Check block fitting
        var blockrow = row - row % 3
        var blockcol = col - col % 3
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (cells[blockrow + i][blockcol + j] == value) return false
            }
        }
        return true
    }
}
