package com.zuk0.gaijinsmash.riderz.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.station_info.StationInfoFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StationInfoFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val frag: Fragment = StationInfoFragment()
        val fm: FragmentManager = mActivityRule.getActivity().getSupportFragmentManager()
        fm.beginTransaction().add(R.id.nav_host_fragment, frag).commit()
    }

    @Test
    fun TestTripFragment() {
        onView(withId(R.id.stationInfo_title_textView)).check(matches(isDisplayed()))
        onView(withId(R.id.stationInfo_map_btn)).check(matches(isDisplayed()))
        onView(withId(R.id.stationInfo_attractionTitle_textView))
                .check(matches(isDisplayed()))
                .check(withText(R.string.stationInfo_attractionTitle) as ViewAssertion?)
    }
}