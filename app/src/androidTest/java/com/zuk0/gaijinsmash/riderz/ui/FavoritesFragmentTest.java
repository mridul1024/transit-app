package com.zuk0.gaijinsmash.riderz.ui;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite.FavoritesFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FavoritesFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Fragment frag = new FavoritesFragment();
        FragmentManager fm = mActivityRule.getActivity().getSupportFragmentManager();
        fm.beginTransaction().add(R.id.nav_host_fragment, frag).commit();
    }

    @Test
    public void TestFavoritesFragment() {
        onView(withId(R.id.results_recyclerView)).check(matches((isDisplayed())));
        onView(withId(R.id.bartFavorites_recyclerView)).check(matches((isDisplayed())));
    }
}
