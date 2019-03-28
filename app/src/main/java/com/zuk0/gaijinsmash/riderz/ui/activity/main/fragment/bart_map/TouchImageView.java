package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TouchImageView extends View {

    private Drawable mImage;
    private float posX;
    private float posY;

    private float lastTouchX;
    private float lastTouchY;

    public TouchImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
