package com.example.gaijinsmash.transitapp

import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.filters.MediumTest
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 1. Find the UI component you want to test in an Activity (for example, a sign-in button in the app)
by calling the onView() method, or the onData() method for AdapterView controls.

 2. Simulate a specific user interaction to perform on that UI component, by calling the ViewInteraction.perform() or DataInteraction.perform() method and passing in the user action (for example, click on the sign-in button).
To sequence multiple actions on the same UI component, chain them using a comma-separated list in your method argument.

 3. Repeat the steps above as necessary, to simulate a user flow across multiple activities in the target app.

 4. Use the ViewAssertions methods to check that the UI reflects the expected state or behavior, after these user interactions are performed.

 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.gaijinsmash.transitapp", appContext.packageName)
    }


    @Test
    @SmallTest
    fun demoSmallTest() {
        // do something
    }

    @Test
    @MediumTest
    fun demoMediumTest() {

    }

    @Test
    @LargeTest
    fun demoLargeTest() {
        //do something that takes a long time
        // these tests can be run on CI servers like Jenkins
    }
}
