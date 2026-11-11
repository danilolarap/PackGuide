package com.example.packguide

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.packguide.ui.screens.LuggagePreset
import com.example.packguide.ui.screens.PackingTutorialPanel
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Online integration test: requires access to the three supplied YouTube tutorials. */
@LargeTest
@RunWith(AndroidJUnit4::class)
class TutorialPlaybackTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private fun findWebView(view: View): WebView? {
        if (view is WebView) return view
        if (view is ViewGroup) for (index in 0 until view.childCount) {
            findWebView(view.getChildAt(index))?.let { return it }
        }
        return null
    }

    private fun evaluate(script: String): String {
        var result = ""
        val done = CountDownLatch(1)
        compose.runOnIdle {
            val view = findWebView(compose.activity.window.decorView)
            if (view == null) done.countDown()
            else view.evaluateJavascript(script) { result = it; done.countDown() }
        }
        assertTrue("WebView did not answer JavaScript", done.await(5, TimeUnit.SECONDS))
        return result
    }

    @Test fun personalTutorialPlaysPausesResumesAndCloses() = exerciseTutorial(LuggagePreset.Personal)
    @Test fun cabinTutorialPlaysPausesResumesAndCloses() = exerciseTutorial(LuggagePreset.Cabin)
    @Test fun checkedTutorialPlaysPausesResumesAndCloses() = exerciseTutorial(LuggagePreset.Checked)

    private fun exerciseTutorial(bag: LuggagePreset) {
        val playing = mutableStateOf(false)
        compose.runOnUiThread { compose.activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
        compose.setContent {
            PackGuideTheme {
                LazyColumn {
                    item {
                        PackingTutorialPanel(bag, playing.value,
                            onPlay = { playing.value = true }, onClose = { playing.value = false })
                    }
                }
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("REPRODUCIR TUTORIAL")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
        compose.onNodeWithText("REPRODUCIR TUTORIAL").performClick()
        compose.onNodeWithTag("tutorial-player-${bag.name}").assertIsDisplayed()
        compose.waitUntil(timeoutMillis = 45_000) {
            evaluate("!!(window.tutorialPlayer && tutorialPlayer.getPlayerState && tutorialPlayer.getPlayerState()===1)") == "true"
        }
        assertEquals("true", evaluate("document.getElementById('player').getBoundingClientRect().height >= 199"))
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("PAUSAR").fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
        compose.onNodeWithTag("toggle-tutorial-${bag.name}").performClick()
        compose.waitUntil(timeoutMillis = 10_000) { evaluate("tutorialPlayer.getPlayerState()") == "2" }
        compose.onNodeWithTag("toggle-tutorial-${bag.name}").performClick()
        compose.waitUntil(timeoutMillis = 15_000) { evaluate("tutorialPlayer.getPlayerState()") == "1" }
        compose.onNodeWithTag("close-tutorial-${bag.name}").performClick()
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("REPRODUCIR TUTORIAL")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
        compose.onNodeWithText("REPRODUCIR TUTORIAL").assertIsDisplayed()
        compose.runOnIdle { assertFalse(compose.activity.isFinishing) }
    }
}
