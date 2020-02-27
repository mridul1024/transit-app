package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_map

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentBartMapBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection


class BartMapFragment : DialogFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mDataBinding: FragmentBartMapBinding
    private lateinit var viewModel: BartMapViewModel

    // Train map view
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.bartmap_alert_dialog, container, false)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.initViewModel()
        mDataBinding.bartMapImageView.setOnTouchListener { view1, event ->
            scaleGestureDetector?.onTouchEvent(event)
            true
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BartMapViewModel::class.java)
        viewModel.initBartMap(activity!!, mDataBinding.bartMapImageView)
    }
}
