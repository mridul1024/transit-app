package com.zuk0.gaijinsmash.riderz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.view.activity.MainActivity;
import com.zuk0.gaijinsmash.riderz.view.fragment.HomeFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test that app launches properly to the Home Fragment - then
 *
 * Espresso testing Framework for API level 10+ with JUnit 4
 * provides automatic synchronization of test actions with the UI of the app
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment frag = new HomeFragment();
        FragmentManager fm = mActivityRule.getActivity().getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContent, frag).commit();
    }

    @Test
    public void TestHomeView() {
        onView(withId(R.id.fragment_home_container)).check(matches((isDisplayed())));
        onView(withId(R.id.home_banner_imageView)).check(matches((isDisplayed())));
        onView(withId(R.id.home_bsa_listView)).check(matches((isDisplayed())));
    }
}


