package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import javax.inject.Singleton

@Singleton
class MainViewModel: ViewModel() {

    fun getHour() : Int {
        return TimeDateUtils.getCurrentHour();
    }

    internal fun initPic(context: Context, hour: Int, imageView: ImageView) {
        Glide.with(context)
                .load(R.drawable.sf_skyline)
                .into(imageView)
        if (hour < 6 || hour >= 21) {
            // show night picture
            imageView.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.nighttime_bg, context.theme))
        } else if (hour >= 17) {
            // show dusk picture
            imageView.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.dusktime_bg, context.theme))
        } else {
            imageView.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.daytime_bg, context.theme))
        }
    }
}