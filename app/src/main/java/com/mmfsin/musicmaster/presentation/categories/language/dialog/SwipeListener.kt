package com.mmfsin.musicmaster.presentation.categories.language.dialog

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class SwipeListener(private val onSwipe: (direction: Direction) -> Unit) :
    GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val diffX = e2!!.x - e1!!.x
        val diffY = e2.y - e1.y

        if (abs(diffX) > abs(diffY)) {
            // Horizontal swipe
            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) onSwipe(Direction.RIGHT)
                else onSwipe(Direction.LEFT)
                return true
            }
        } else {
            // Vertical swipe
            if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) onSwipe(Direction.BOTTOM)
                else onSwipe(Direction.TOP)
                return true
            }
        }
        return false
    }

    enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
