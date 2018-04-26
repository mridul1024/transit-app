package com.zuk0.gaijinsmash.riderz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.activity.MainActivity;
import com.zuk0.gaijinsmash.riderz.fragment.FavoritesFragment;

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
public class FavoritesFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment frag = new FavoritesFragment();
        FragmentManager fm = mActivityRule.getActivity().getFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContent, frag).commit();
    }

    @Test
    public void TestFavoritesFragment() {
        onView(withId(R.id.results_listView)).check(matches((isDisplayed())));
        onView(withId(R.id.bartFavorites_listView)).check(matches((isDisplayed())));
    }
}
