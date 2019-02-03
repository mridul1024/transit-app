package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartMapViewModel extends AndroidViewModel {

    @Inject
    public BartMapViewModel(Application application) {
        super(application);
    }

    public void initBartMap(Context context, ImageView imageView) {

        Drawable img;

        //if sunday show sunday map -
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 7) {
            img = context.getDrawable(R.drawable.bart_map_sunday);
        } else {
            img = context.getDrawable(R.drawable.bart_map_weekday_sat);
        }
        Glide.with(context)
                .load(img)
                .into(imageView);
    }
}
