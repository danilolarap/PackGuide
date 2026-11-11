package com.example.packguide.ui.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import org.json.JSONObject

internal val LuggagePreset.tutorialVideoId: String
    get() = when (this) {
        LuggagePreset.Personal -> "Sb3_ZvMiCEM"
        LuggagePreset.Cabin -> "mG0t34yy9sE"
        LuggagePreset.Checked -> "1YF7l-JL06M"
    }

/** Defers native teardown until Compose has finished removing the AndroidView. */
internal class TutorialPlayerController {
    private val main = Handler(Looper.getMainLooper())
    var view: WebView? = null
    var playerState by mutableIntStateOf(-1)
        private set
    val isPlaying: Boolean get() = playerState == 1 || playerState == 3
    private var released = false

    @Keep
    class StateBridge(private val onState: (Int) -> Unit) {
        @JavascriptInterface
        fun stateChanged(state: Int) = onState(state)
    }

    val bridge = StateBridge { state ->
        main.post { if (!released) playerState = state }
        Unit
    }

    fun togglePause() {
        val command = if (isPlaying) "pauseTutorial()" else "playTutorial()"
        view?.evaluateJavascript(command, null)
    }

    fun prepareToClose() {
        view?.evaluateJavascript("pauseTutorial()", null)
        view?.clearFocus()
    }

    fun release(webView: WebView) {
        released = true
        view = null
        // Do not remove a focused native view or destroy its children during a Compose apply.
        main.post {
            webView.clearFocus()
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.onPause()
            webView.removeJavascriptInterface("TutorialState")
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.destroy()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun PackingTutorialPanel(type: LuggagePreset, playing: Boolean, onPlay: () -> Unit, onClose: () -> Unit) {
    var attempt by remember { mutableIntStateOf(0) }
    val controller = remember(playing, attempt) { TutorialPlayerController() }
    var failed by remember(playing) { mutableStateOf(false) }
    val focus = LocalFocusManager.current
    val language = LocalAppLanguage.current
    val error = localizedText("No se pudo reproducir el tutorial. Revisa tu conexión o inténtalo de nuevo más tarde.", language)
    // Keep the native video surface outside clipped graphics layers.
    Column(
        Modifier.fillMaxWidth().background(PackGuideColors.surface, RoundedCornerShape(18.dp))
            .border(1.dp, PackGuideColors.border, RoundedCornerShape(18.dp)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("TUTORIAL DE EMPACADO", style = Typography.labelSmall, color = PackGuideColors.secondaryText)
        Text("Cómo guardar y doblar la ropa", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        val playerModifier = Modifier.fillMaxWidth().height(220.dp)
        if (playing && !failed) {
            AndroidView(
                modifier = playerModifier.testTag("tutorial-player-${type.name}"),
                factory = { context ->
                    WebView(context).apply {
                        controller.view = this
                        // Touch controls work without transferring keyboard focus into the lazy layout.
                        isFocusable = false
                        isFocusableInTouchMode = false
                        setBackgroundColor(android.graphics.Color.BLACK)
                        setLayerType(View.LAYER_TYPE_HARDWARE, null)
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        addJavascriptInterface(controller.bridge, "TutorialState")
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
                                request.isForMainFrame || request.url.scheme != "https"

                            override fun onReceivedError(view: WebView, request: WebResourceRequest, reason: WebResourceError) {
                                if (request.isForMainFrame) failed = true
                            }
                        }
                        val origin = "https://${context.packageName}"
                        loadDataWithBaseURL(origin + "/", tutorialHtml(type.tutorialVideoId, origin, error), "text/html", "UTF-8", null)
                    }
                },
                onRelease = controller::release
            )
        } else {
            Surface(onClick = { failed = false; attempt++; onPlay() }, shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer, modifier = playerModifier) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)) {
                    Icon(Icons.Outlined.PlayCircle, contentDescription = null, modifier = Modifier.size(52.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(if (failed) "REINTENTAR TUTORIAL" else "REPRODUCIR TUTORIAL",
                        style = Typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    if (failed) Text(error, color = MaterialTheme.colorScheme.onPrimaryContainer, style = Typography.bodySmall)
                }
            }
        }
        if (playing) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AsciiButton(
                    if (controller.isPlaying) "PAUSAR" else "CONTINUAR",
                    icon = if (controller.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                    enabled = controller.playerState >= 0 && !failed,
                    onClick = controller::togglePause,
                    modifier = Modifier.weight(1f).testTag("toggle-tutorial-${type.name}")
                )
                TextButton(onClick = {
                    controller.prepareToClose()
                    focus.clearFocus(force = true)
                    onClose()
                }, modifier = Modifier.testTag("close-tutorial-${type.name}")) { Text("CERRAR VIDEO") }
            }
        }
        Text("Requiere conexión a internet.", style = Typography.bodySmall, color = PackGuideColors.secondaryText)
    }
}

internal fun tutorialHtml(videoId: String, origin: String, error: String): String = """
    <!doctype html><html><head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="referrer" content="strict-origin-when-cross-origin">
    <style>
    html,body{margin:0;padding:0;width:100vw;height:100vh;overflow:hidden;background:#191919;color:white}
    #player{position:fixed;left:0;top:0;width:100vw;height:100vh;border:0;display:block}
    #error{display:none;padding:20px;font:16px sans-serif;box-sizing:border-box}
    </style></head><body><div id="player"></div><div id="error" role="alert"></div>
    <script>
    var tutorialPlayer;
    function reportState(state) {
        if(window.TutorialState) TutorialState.stateChanged(state);
    }
    function pauseTutorial(){if(tutorialPlayer && tutorialPlayer.pauseVideo) tutorialPlayer.pauseVideo();}
    function playTutorial(){if(tutorialPlayer && tutorialPlayer.playVideo) tutorialPlayer.playVideo();}
    function resizeTutorial() {
        if (!tutorialPlayer || !tutorialPlayer.getIframe) return;
        var width=window.innerWidth, height=window.innerHeight;
        tutorialPlayer.setSize(width,height);
        var frame=tutorialPlayer.getIframe();
        frame.style.width=width+'px'; frame.style.height=height+'px';
        frame.style.position='fixed'; frame.style.left='0'; frame.style.top='0';
    }
    window.addEventListener('resize',resizeTutorial);
    function showError() {
        document.getElementById('player').style.display='none';
        var error=document.getElementById('error');
        error.textContent=${JSONObject.quote(error)}; error.style.display='block';
        reportState(-1);
    }
    function onYouTubeIframeAPIReady() {
        tutorialPlayer = new YT.Player('player', {
            width:window.innerWidth,height:window.innerHeight,videoId:'$videoId',
            playerVars:{autoplay:1,playsinline:1,controls:1,fs:0,origin:'$origin'},
            events:{
                onReady:function(){resizeTutorial();reportState(5);},
                onStateChange:function(event){reportState(event.data);},
                onError:showError
            }
        });
    }
    </script><script src="https://www.youtube.com/iframe_api" onerror="showError()"></script>
    </body></html>
""".trimIndent()
