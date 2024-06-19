package com.juanasoco.compasschallenge.presentation

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.view.WindowCompat
import com.juanasoco.compasschallenge.MainActivity
import com.juanasoco.compasschallenge.di.AppModule
import com.juanasoco.compasschallenge.ui.theme.CompassChallengeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MainScreenCachedDataTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            WindowCompat.setDecorFitsSystemWindows(composeRule.activity.window, false)
            CompassChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Test
    fun displayCachedContent(){
        composeRule.onNodeWithText("Fetch Content").performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onNodeWithText("0123456789: 2").isDisplayed()
            composeRule.onNodeWithText("12345: 2").isDisplayed()
        }
    }

}