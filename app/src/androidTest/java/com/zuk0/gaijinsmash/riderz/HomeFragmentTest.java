package com.zuk0.gaijinsmash.riderz;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import com.zuk0.gaijinsmash.riderz.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that app launches properly to the Home Fragment - then
 *
 * Espresso testing Framework for API level 10+ with JUnit 4
 * provides automatic synchronization of test actions with the UI of the app
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeFragmentTest {


    private Button mButton;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testNearbyStationsButton() {

    }
}


