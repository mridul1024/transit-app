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
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test that app launches properly to the Home Fragment - then
 *
 * Espresso testing Framework for API level 10+ with JUnit 4
 * provides automatic synchronization of test actions with the UI of the app
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val frag: Fragment = HomeFragment()
        val fm: FragmentManager = mActivityRule.getActivity().getSupportFragmentManager()
        fm.beginTransaction().add(R.id.nav_host_fragment, frag).commit()
    }

    @Test
    fun TestHomeView() {
        //onView(withId(R.id.main_nested_scrollView)).check(matches((isDisplayed())));
        onView(withId(R.id.main_banner_imageView)).check(matches(isDisplayed()))
        //onView(withId(R.id.home_bsa_listView)).check(matches((isDisplayed())));
    }
}