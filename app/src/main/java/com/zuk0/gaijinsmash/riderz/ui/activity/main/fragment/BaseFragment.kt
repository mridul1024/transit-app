package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.di.component.FragmentComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    // ---------------------------------------------------------------------------------------------
    // Lifecycle Events
    // ---------------------------------------------------------------------------------------------

    override fun onAttach(context: Context) {
        initDagger() // this must go before the super method
        super.onAttach(context)
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

    override fun onStart() {
        super.onStart()
        Logger.d("onStart()")
    }

    override fun onResume() {
        super.onResume()
        expandBottomNav()
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
        Logger.d("onPause()")
        super.onPause()
    }

    override fun onStop() {
        Logger.d("onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Logger.d("onDestroy()")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Logger.d("onDestroyView()")
        super.onDestroyView()
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

    //todo - deprecate
    fun enableNestedScrolling(enable: Boolean) {
        //val nestedScrollView = activity?.findViewById<NestedScrollView>(R.id.main_nested_scrollView)
        //nestedScrollView?.isNestedScrollingEnabled = enable
    }

    fun expandBottomNav() {
        activity?.let {
            val bottomNav: BottomNavigationView = it.findViewById(R.id.main_bottom_navigation)
            //bottomNav.isVisible
            //val behavior = HideBottomViewOnScrollBehavior.getTag(bottomNav) as HideBottomViewOnScrollBehavior<BottomNavigationView>
            val behavior = HideBottomViewOnScrollBehavior<BottomNavigationView>()
            behavior.slideUp(bottomNav)

        }
    }

    fun setTitle(activity: Activity?, title: String) {
        activity?.let {
            val collapsingToolbarLayout: CollapsingToolbarLayout = activity.findViewById(R.id.main_collapsing_toolbar)
            collapsingToolbarLayout.title = title
        }
    }
}