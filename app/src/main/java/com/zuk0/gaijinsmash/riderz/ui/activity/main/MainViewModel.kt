package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.widget.ImageView
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
        if (hour < 6 || hour >= 21) {
            // show night picture
            Glide.with(context)
                    .load(R.drawable.sf_night)
                    .into(imageView)
        } else if (hour >= 17) {
            // show dusk picture
            Glide.with(context)
                    .load(R.drawable.sf_dusk)
                    .into(imageView)
        } else {
            Glide.with(context)
                    .load(R.drawable.sf_day)
                    .into(imageView)
        }
    }
}