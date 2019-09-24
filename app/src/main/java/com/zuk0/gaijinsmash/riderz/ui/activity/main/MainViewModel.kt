package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import javax.inject.Singleton

@Singleton
class MainViewModel: ViewModel() {

    val hour: Int
        get() {
           return TimeDateUtils.getCurrentHour()
        }

    fun getColorScheme(context: Context, hour: Int) : Int {
        var color = 0
        if (hour < 6 || hour >= 21) {
            // show night picture
            color = ContextCompat.getColor(context, R.color.white)
        } else if (hour >= 17) {
            // show dusk picture
            color = ContextCompat.getColor(context, R.color.white)
        } else {
            color = ContextCompat.getColor(context, R.color.black)
        }
        return color
    }

    fun getBackgroundDrawable(context: Context, hour: Int) : Drawable? {
        val bg: Drawable?
        if (hour < 6 || hour >= 21) {
            // show night picture
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_night)
        } else if (hour >= 17) {
            // show dusk picture
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_dusk)
        } else {
            bg = ContextCompat.getDrawable(context, R.drawable.gradient_day)
        }
        return bg
    }

    /**
     * Save UI state
     */
    fun saveState() {

    }

    /**
     * Restore UI state
     */
    fun restoreState(savedInstanceState: Bundle) {
        //todo
    }
}