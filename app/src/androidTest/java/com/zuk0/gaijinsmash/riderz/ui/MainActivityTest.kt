package com.zuk0.gaijinsmash.riderz.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class MainActivityTest {
    /*
        The Rule will make sure to launch the MainActivity directly.
    */
    @Rule
    var activityActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setup() {
        activityActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
    }

    @Test
    fun drawerTest() {
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun bottomNavTest() {
        onView(withId(R.id.main_bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test
    fun actionBarTest() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed())).perform(click())
    }
}