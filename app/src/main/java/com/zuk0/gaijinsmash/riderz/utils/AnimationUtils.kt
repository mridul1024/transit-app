package com.zuk0.gaijinsmash.riderz.utils

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation

object AnimationUtils {

    /**
     * Sets a fling animation for view
     */
    fun fling(view: View, velocityX: Float, frict: Float = 1.1f, maxScroll: Float) {
        FlingAnimation(view, DynamicAnimation.SCROLL_X).apply {
            setStartVelocity(-velocityX)
            setMinValue(0f)
            setMaxValue(maxScroll)
            friction = frict
            start()
        }
    }
}