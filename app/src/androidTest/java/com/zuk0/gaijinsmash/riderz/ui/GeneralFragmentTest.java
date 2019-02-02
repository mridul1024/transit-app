package com.zuk0.gaijinsmash.riderz.ui;


import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
        onView(withId(R.id.nav_host_fragment)).check(matches((isDisplayed())));
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
