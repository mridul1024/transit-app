package com.zuk0.gaijinsmash.riderz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.activity.MainActivity;
import com.zuk0.gaijinsmash.riderz.fragment.FavoritesFragment;
import com.zuk0.gaijinsmash.riderz.fragment.StationInfoFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StationInfoFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment frag = new StationInfoFragment();
        FragmentManager fm = mActivityRule.getActivity().getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContent, frag).commit();
    }

    @Test
    public void TestTripFragment() {
        onView(withId(R.id.stationInfo_title_textView)).check(matches((isDisplayed())));
        onView(withId(R.id.stationInfo_map_btn)).check(matches((isDisplayed())));
        onView(withId(R.id.stationInfo_attractionTitle_textView))
                .check(matches((isDisplayed())))
                .check((ViewAssertion) withText(R.string.stationInfo_attractionTitle));
    }
}
