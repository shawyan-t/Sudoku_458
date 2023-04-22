package com.example.sudoku_458

import android.content.Context
import android.gesture.Gesture
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

class MyGrid(context: Context?, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener {

    private var grWidth: Float = 0.0f
    private var grHeight: Float = 0.0f
    private var grMidWidth: Float = 0.0f
    private var grMidHeight: Float = 0.0f

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
    private val grid = Array(gridSize) { IntArray(gridSize) }

    private val numTextProperties = Paint().apply {
        color = Color.BLACK; textSize = (gridSize * 16f); textAlign = Paint.Align.CENTER }

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

        for (i in 1..drawAdjusted) {
            for (j in 1..drawAdjusted) {
                val text = grid[i][j].toString()
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

        // Restart button randomizes a new sudoku board and resets all flags and tips and stuff
        val text = "Restart"
        canvas.drawText(
            text,
            ((1.5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )

        // Will have an array of different small tips
        // When tapped, the tipsText variable changes to one of those tips in the array
        // can keep tapping for different tips
        val tipsText = "Tips"
        canvas.save() // Save the current state of the canvas
        canvas.rotate(90f, 30f, 240f) // Rotate the canvas around the specified point
        canvas.drawText(tipsText, 30f, 240f, playerTextDisplays) // Draw the text
        canvas.restore() // Restore the previous state of the canvas

        // This text only needs to be changed if Sudoku is solved. Game Logic checker will change text here if flag is met
        val playerSolvedtext = "Unsolved"
        canvas.drawText(
            playerSolvedtext,
            ((5).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )

        // I was too lazy to add this tonight but basically a timer needs to go here
        val timer = "Timer here"
        canvas.drawText(
            timer,
            ((8.23).toFloat() * horizontalBoundary) + (horizontalBoundary / 2),
            100f,
            playerTextDisplays
        )

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

        val column = (p0.x / grWidth * gridSize).toInt()
        val row = (p0.y / grHeight * gridSize).toInt()

        /* Use Modulo operation to increment grid values by 1 up to 9
            resets to 0 upon 10 clicks
         */

        grid[column][row] = (grid[column][row] + 1) % 10

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
