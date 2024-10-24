package com.example.roboletric

import android.content.Intent
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.TIRAMISU],
    qualifiers = "fr-rFR-w360dp-h640dp-xhdpi"
)
class RobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var activity: ActivityController<MainActivity>
    @Before
    fun setup() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
    }

    @Test
    fun testDynamicReceiver() {

        activity.create().start().resume()

        assertNotNull(activity.get().receiver)

        activity.pause().stop()

        assertNull(activity.get().receiver)
    }

    @Test
    fun clickStart_shouldShowSecondScreen_testWithAndroidx() {
        ActivityScenario.launch(MainActivity::class.java)
            .use {scenario ->
                scenario.onActivity {
                    composeTestRule.onNodeWithText("Start").performClick()
                    composeTestRule.onNodeWithText("Send").assertIsDisplayed()
                }
            }
    }

    @Test
    fun clickSend_shoulStartIntent_SEND() {
        ActivityScenario.launch(MainActivity::class.java)
            .use {scenario ->
                scenario.onActivity {
                    composeTestRule.setContent {
                        SecondScreen(text = "Hello", onTextChange = {})
                    }
                    composeTestRule.onNodeWithText("Send").performClick()

                    val startedIntent = Shadows.shadowOf(activity.start().get()).nextStartedActivity

                    assert(startedIntent.action == Intent.ACTION_SEND)
                    assert(startedIntent.getStringExtra(Intent.EXTRA_TEXT) == "Hello")
                    assert(startedIntent.type == "text/plain")
                }
            }
    }
}