package com.zuk0.gaijinsmash.riderz;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GeneralFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void TestMainActivity() {
        onView(withId(R.id.fragmentContent)).check(matches((isDisplayed())));
    }

    @Test
    public void TestBottomNavigation() {
        onView(withId(R.id.bottom_navigation))
                .check(matches((isDisplayed())));
    }

    @Test
    public void TestToolbar() {
        onView(withId(R.id.toolbar))
                .check(matches((isDisplayed())));
    }

    @Test
    public void TestNavDrawerActions() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).perform(click());
    }

}
