package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TouchImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val mImage: Drawable? = null
    private val posX = 0f
    private val posY = 0f
    private val lastTouchX = 0f
    private val lastTouchY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }
}