package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDagger()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d("onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Logger.d("onActivityCreated()")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("onResume()")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Logger.d("onViewRestored()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Logger.d("onSaveInstanceState()")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("onPause()")
    }

    override fun onStop() {
        super.onStop()
        Logger.d("onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("onDestroy()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.d("onDestroyView()")
    }

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    private fun initDagger() {
        AndroidSupportInjection.inject(this)
    }

    fun collapseAppBar(activity: Activity?) {
        activity?.let {
            val appBarLayout: AppBarLayout = activity.findViewById(R.id.main_app_bar_layout)
            appBarLayout.setExpanded(false)
        }
    }

    fun expandAppBar(activity: Activity?) {
        activity?.let {
            val appBarLayout: AppBarLayout = activity.findViewById(R.id.main_app_bar_layout)
            appBarLayout.setExpanded(true)
        }
    }

    fun enableNestedScrolling(enable: Boolean) {
        val nestedScrollView = activity?.findViewById<NestedScrollView>(R.id.main_nested_scrollView)
        nestedScrollView?.isNestedScrollingEnabled = enable
    }
}