package com.zuk0.gaijinsmash.riderz.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.help.HelpFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelpFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val frag: Fragment = HelpFragment()
        val fm: FragmentManager = mActivityRule.getActivity().getSupportFragmentManager()
        fm.beginTransaction().add(R.id.nav_host_fragment, frag).commit()
    }

    @Test
    fun TestFavoritesFragment() {
        onView(withId(R.id.help_cardView)).check(matches(isDisplayed()))
        onView(withId(R.id.help_report_button)).check(matches(isDisplayed()))
    }
}