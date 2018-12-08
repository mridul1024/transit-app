package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartMapViewModel extends AndroidViewModel {

    @Inject
    public BartMapViewModel(Application application) {
        super(application);
    }

    public void initBartMap(Context context, ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.bart_cc_map2)
                .into(imageView);
    }
}
