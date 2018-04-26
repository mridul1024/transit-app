package com.zuk0.gaijinsmash.riderz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.activity.MainActivity;
import com.zuk0.gaijinsmash.riderz.fragment.StationFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StationFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment frag = new StationFragment();
        FragmentManager fm = mActivityRule.getActivity().getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContent, frag).commit();
    }

    @Test
    public void TestStationFragment() {
        onView(withId(R.id.station_listView)).check(matches((isDisplayed())));
    }
}
