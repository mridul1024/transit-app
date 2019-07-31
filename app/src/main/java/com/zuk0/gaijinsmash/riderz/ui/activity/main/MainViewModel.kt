package com.zuk0.gaijinsmash.riderz.ui.activity.main

import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.MainActivityBinding
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils
import javax.inject.Singleton

@Singleton
class MainViewModel: ViewModel() {

    fun getHour() : Int {
        return TimeDateUtils.getCurrentHour();
    }

    internal fun initPic(context: Context, hour: Int, binding: MainActivityBinding) {
        Glide.with(context)
                .load(R.drawable.sf_skyline)
                .into(binding.mainBannerImageView)
        if (hour < 6 || hour >= 21) {
            // show night picture
            binding.imageBackground.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.nighttime_bg, context.theme))
        } else if (hour >= 17) {
            // show dusk picture
            binding.imageBackground.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.dusktime_bg, context.theme))
        } else {
            binding.imageBackground.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.daytime_bg, context.theme))
        }
    }
}