package com.zuk0.gaijinsmash.riderz

import android.view.ViewGroup
import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.crashlytics.android.Crashlytics
import com.zuk0.gaijinsmash.riderz.ui.activity.test.TestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class FirebaseTest {

    lateinit var crashButton: Button

    @JvmField
    @Rule
    val testActivityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        initCrashButton()
    }

    private fun initCrashButton() {
        crashButton = Button(testActivityRule.activity)
        crashButton.id = R.id.crash_button
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            Crashlytics.getInstance().crash() // force a crash
        }

        testActivityRule.activity.addContentView(crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ))
    }

    @Test
    fun pressCrashButton_and_verifyCrashlytics() {
        onView(withId(R.id.crash_button))
                .check(matches((isDisplayed())))
                .perform(click())
    }

}