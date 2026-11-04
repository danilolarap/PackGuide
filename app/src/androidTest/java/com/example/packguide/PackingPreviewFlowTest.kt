package com.example.packguide

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.app.ActivityOptionsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.packguide.ai.*
import com.example.packguide.scanning.PhotoProblem
import com.example.packguide.scanning.PhotoReadException
import com.example.packguide.scanning.PhotoScanner
import com.example.packguide.settings.AppLanguage
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.screens.*
import com.example.packguide.ui.theme.PackGuideTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.CRC32

@RunWith(AndroidJUnit4::class)
class PackingPreviewFlowTest {
    @get:Rule val compose = createComposeRule()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    private class PhotoRegistry : ActivityResultRegistry(), ActivityResultRegistryOwner {
        var nextPhoto: Uri? = null
        var launches = 0
        override val activityResultRegistry: ActivityResultRegistry get() = this
        override fun <I, O> onLaunch(requestCode: Int, contract: ActivityResultContract<I, O>, input: I,
                                      options: ActivityOptionsCompat?) {
            launches++
            dispatchResult(requestCode, if (nextPhoto == null) Activity.RESULT_CANCELED else Activity.RESULT_OK,
                Intent().setData(nextPhoto))
        }
    }

    private fun photoBytes(): ByteArray {
        val photo = Bitmap.createBitmap(1000, 700, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(photo)
        canvas.drawColor(Color.LTGRAY)
        val paint = Paint().apply { color = Color.DKGRAY }
        canvas.drawRoundRect(70f, 70f, 930f, 630f, 45f, 45f, paint)
        paint.color = Color.rgb(218, 208, 188)
        canvas.drawRect(100f, 100f, 485f, 600f, paint)
        canvas.drawRect(515f, 100f, 900f, 600f, paint)
        return ByteArrayOutputStream().use { stream ->
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
            photo.recycle()
            stream.toByteArray()
        }
    }

    private fun addFixture(bytes: ByteArray): Uri {
        // This test creates and later deletes its own media. No broad storage permission.
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "packguide-preview-test-${System.nanoTime()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = requireNotNull(context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values))
        context.contentResolver.openOutputStream(uri)!!.use { it.write(bytes) }
        return uri
    }

    private fun scrollTo(label: String) {
        compose.onNode(hasScrollAction()).performScrollToNode(hasText(label))
    }

    @Test fun pickerCancellationPhotoGenerationViewerAndNavigationNeedNoCredentials() {
        val uri = addFixture(photoBytes())
        try {
            val registry = PhotoRegistry()
            compose.setContent {
                CompositionLocalProvider(LocalActivityResultRegistryOwner provides registry) {
                    PackGuideTheme { MainScreen(AppSettings(), {}, {}, {}) }
                }
            }
            compose.onNodeWithText("GUÍA").performClick()
            scrollTo("AÑADIR FOTO EN ESCÁNER")
            compose.onNodeWithText("AÑADIR FOTO EN ESCÁNER").performClick()
            scrollTo("ELEGIR FOTO")
            compose.onNodeWithText("ELEGIR FOTO").performClick() // Cancel first.
            compose.runOnIdle { assertEquals(1, registry.launches); registry.nextPhoto = uri }
            compose.onNodeWithText("ELEGIR FOTO").performClick()
            compose.waitUntil(10_000) { compose.onAllNodesWithContentDescription("Foto de tu maleta").fetchSemanticsNodes().isNotEmpty() }
            compose.onNodeWithText("GUÍA").performClick()
            scrollTo("GENERAR IMAGEN ORGANIZADA")
            compose.onNodeWithText("GENERAR IMAGEN ORGANIZADA").assertIsEnabled().performClick()
            compose.waitUntil(10_000) { compose.onAllNodesWithText("Ampliar vista organizada").fetchSemanticsNodes().isNotEmpty() }
            compose.onAllNodesWithText("CONEXIÓN CON GEMINI", substring = true).assertCountEquals(0)
            compose.onAllNodesWithText("CONFIGURAR IA").assertCountEquals(0)
            compose.onNodeWithText("Ampliar vista organizada").performScrollTo().performClick()
            compose.onNodeWithText("CERRAR").performClick()
            compose.onNodeWithText("RESUMEN").performClick()
            compose.onAllNodesWithText("CONFIGURAR IA").assertCountEquals(0)
            compose.onNodeWithText("GUÍA").performClick()
            scrollTo("Ampliar vista organizada")
            compose.onNodeWithText("Ampliar vista organizada").assertExists()
        } finally { context.contentResolver.delete(uri, null, null) }
    }

    @Test fun contentPhotosAreBoundedAndUnsupportedOrOversizedInputsAreRejected() = runBlocking {
        val good = addFixture(photoBytes())
        val bad = addFixture("not an image".toByteArray())
        val oversizedBytes = photoBytes()
        ByteBuffer.wrap(oversizedBytes).putInt(16, 20_000).putInt(20, 20_000)
        val crc = CRC32().apply { update(oversizedBytes, 12, 17) }.value
        ByteBuffer.wrap(oversizedBytes).putInt(29, crc.toInt())
        val large = addFixture(oversizedBytes)
        try {
            val decoded = PhotoScanner().decode(context, good)
            assertEquals(1000, decoded.width)
            decoded.recycle()
            for ((uri, expected) in listOf(bad to PhotoProblem.Unsupported, large to PhotoProblem.TooLarge)) {
                try { PhotoScanner().decode(context, uri); fail("Should reject invalid photo") }
                catch (error: PhotoReadException) { assertEquals(expected, error.problem) }
            }
        } finally { listOf(good, bad, large).forEach { context.contentResolver.delete(it, null, null) } }
    }

    @Test fun failuresDuplicateRequestsAndCancellationPreserveStateAndAllowRetry() {
        compose.setContent { PackGuideTheme { androidx.compose.material3.Text("Preview test") } }
        val plan = PackingPlan.from(PackingState().activeBag)
        val photo = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
        val pending = CompletableDeferred<Bitmap>()
        var requests = 0
        val generator = PackingImageService { _, _, _ -> requests++; pending.await() }
        val vm = PackingPreviewViewModel(context.applicationContext as Application, generator)
        val type = LuggagePreset.Cabin
        compose.runOnIdle {
            vm.generate(type, plan, photo, AppLanguage.Spanish)
            vm.generate(type, plan, photo, AppLanguage.Spanish)
            assertEquals(1, requests)
            assertEquals(PreviewWork.Guide, vm.stateFor(type).work)
            vm.cancel(type)
        }
        compose.runOnIdle {
            assertNull(vm.stateFor(type).work)
            assertEquals(PreviewProblem.Cancelled, vm.stateFor(type).problem)
            assertNull(vm.stateFor(type).guide)
        }
        var shouldFail = true
        val retry = PackingPreviewViewModel(context.applicationContext as Application, PackingImageService { _, _, _ ->
            if (shouldFail) throw IOException("Test service unavailable") else photo
        })
        compose.runOnIdle { retry.generate(type, plan, photo, AppLanguage.English) }
        compose.runOnIdle {
            assertNull(retry.stateFor(type).work)
            assertEquals(PreviewProblem.Connection, retry.stateFor(type).problem)
            shouldFail = false
            retry.generate(type, plan, photo, AppLanguage.English)
        }
        compose.runOnIdle {
            assertSame(photo, retry.stateFor(type).guide)
            assertNull(retry.stateFor(type).problem)
            retry.clear(); vm.clear()
        }
    }

    @Test fun offlineRendererPreservesOriginalReferenceAndCreatesBoundedBitmap() = runBlocking {
        val uri = addFixture(photoBytes())
        try {
            val source = PhotoScanner().decode(context, uri)
            val before = source.getPixel(500, 350)
            val plan = PackingPlan.from(PackingState().activeBag)
            val result = OfflinePackingImageService().generate(plan, source, AppLanguage.Spanish)
            assertEquals(1200, result.width)
            assertEquals(PackingPreviewLayout.from(plan).height, result.height)
            assertEquals(before, source.getPixel(500, 350))
            assertEquals(before, result.getPixel(324, 305))
            java.io.File(context.getExternalFilesDir(null), "packing-preview-example.png").outputStream().use {
                result.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            result.recycle(); source.recycle()
        } finally { context.contentResolver.delete(uri, null, null) }
    }

    @Test fun englishGuideShowsOfflineLabelsAndDirectPhotoAction() {
        val packing = PackingState()
        val scanner = com.example.packguide.scanning.ScannerViewModel(context.applicationContext as Application)
        val preview = PackingPreviewViewModel(context.applicationContext as Application)
        compose.setContent {
            CompositionLocalProvider(LocalAppLanguage provides AppLanguage.English) {
                PackGuideTheme {
                    PackingGuidePreviewPanel(packing, scanner, preview, {}, {})
                }
            }
        }
        compose.onNodeWithText("ORGANIZED PACKING PREVIEW").assertExists()
        compose.onNodeWithText("ADD PHOTO IN SCANNER").assertExists()
        compose.onNodeWithText("GENERATE PACKING IMAGE").assertIsNotEnabled()
        compose.onAllNodesWithText("API", substring = true).assertCountEquals(0)
    }
}
