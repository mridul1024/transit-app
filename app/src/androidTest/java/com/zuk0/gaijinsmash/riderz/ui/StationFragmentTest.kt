package com.zuk0.gaijinsmash.riderz.ui

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations.StationsFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StationFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val frag: Fragment = StationsFragment()
        //todo: fix this
        //FragmentManager fm = mActivityRule.getActivity().getFragmentManager();
        //fm.beginTransaction().save(R.id.fragmentContent, frag).commit();
    }

    @Test
    fun TestStationFragment() {
        onView(withId(R.id.station_recyclerView)).check(matches(isDisplayed()))
    }
}