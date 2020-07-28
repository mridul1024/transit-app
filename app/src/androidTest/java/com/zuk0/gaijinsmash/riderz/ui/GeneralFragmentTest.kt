package com.zuk0.gaijinsmash.riderz.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class GeneralFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
    }

    @Test
    fun TestMainActivity() {
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun TestBottomNavigation() {
        onView(withId(R.id.main_bottom_navigation))
                .check(matches(isDisplayed()))
    }

    @Test
    fun TestToolbar() {
        onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()))
    }

    @Test
    fun TestNavDrawerActions() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.drawer_layout)).perform(click())
    }
}