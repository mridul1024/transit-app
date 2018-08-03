package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;

import javax.inject.Singleton;

@Singleton
public class BartMapViewModel extends ViewModel {

    public void initBarMap(Context context, ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.bart_map)
                .into(imageView);
    }
}
