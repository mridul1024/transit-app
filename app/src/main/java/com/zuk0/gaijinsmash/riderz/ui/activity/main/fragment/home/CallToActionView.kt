package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.zuk0.gaijinsmash.riderz.databinding.CallToActionBinding

class CallToActionView: View, LifecycleObserver {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding: CallToActionBinding

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CallToActionBinding.inflate(inflater)
        initListeners()
    }

    /*
        Lifecycle Callbacks
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        // initialize here
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        // save state here
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        // restore state here
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        //clean up here
    }

    fun initListeners() {
        binding.callToAction.setOnClickListener { view ->

        }
        binding.callToActionSpinner.setOnClickListener { view ->

        }
    }
}