package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.zuk0.gaijinsmash.riderz.R
import java.util.*
import javax.inject.Inject

class BartMapViewModel
@Inject constructor(application: Application) : AndroidViewModel(application) {

    fun initBartMap(context: Context, imageView: ImageView?) {
        val img: Drawable?
        val cal = Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US)
        val day = cal[Calendar.DAY_OF_WEEK]
        img = if (day == 7) {
            context.getDrawable(R.drawable.bart_map_sunday)
        } else {
            context.getDrawable(R.drawable.bart_map_weekday_sat)
        }
        Glide.with(context)
                .load(img)
                .into(imageView!!)
    }
}