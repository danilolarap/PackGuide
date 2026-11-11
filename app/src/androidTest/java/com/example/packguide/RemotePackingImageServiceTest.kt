package com.example.packguide

import android.graphics.Bitmap
import android.util.Base64
import com.example.packguide.ai.*
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.screens.PackingState
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class RemotePackingImageServiceTest {
    private class Connection(url: URL, private val response: String) : HttpURLConnection(url) {
        val sent = ByteArrayOutputStream()
        override fun connect() {}
        override fun disconnect() {}
        override fun usingProxy() = false
        override fun getResponseCode() = 200
        override fun getOutputStream() = sent
        override fun getInputStream() = ByteArrayInputStream(response.toByteArray())
    }

    private fun response(quantity: Int): String {
        val photo = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
        val encoded = ByteArrayOutputStream().use {
            photo.compress(Bitmap.CompressFormat.JPEG, 80, it)
            Base64.encodeToString(it.toByteArray(), Base64.NO_WRAP)
        }
        photo.recycle()
        return JSONObject().put("schemaVersion", 1).put("mimeType", "image/jpeg").put("imageBase64", encoded)
            .put("packedItems", JSONArray().put(JSONObject().put("id", "ITEM-01").put("packedQuantity", quantity))).toString()
    }

    @Test fun androidSendsElevenPackedUnitsAndNeverSendsTwelveSelectedUnits() = runBlocking {
        val packing = PackingState()
        packing.updateItem("ITEM-01") { it.copy(quantity = 12, packedQuantity = 11) }
        val plan = PackingPlan.from(packing.activeBag)
        val connection = Connection(URL("https://example.test/v1/packing-image"), response(11))
        val service = RemotePackingImageService("https://example.test", openConnection = { connection })
        val image = service.generate(plan, AppLanguage.Spanish)
        val request = JSONObject(connection.sent.toString(Charsets.UTF_8.name()))
        assertEquals(1, request.getJSONArray("items").length())
        val item = request.getJSONArray("items").getJSONObject(0)
        assertEquals(11, item.getInt("packedQuantity"))
        assertFalse(item.has("quantity"))
        assertFalse(item.has("selectedQuantity"))
        assertFalse(request.has("photo"))
        assertEquals(32, image.width)
        image.recycle()
    }

    @Test fun mismatchedServerInventoryIsRejectedInsteadOfDisplayed() = runBlocking {
        val packing = PackingState()
        packing.updateItem("ITEM-01") { it.copy(quantity = 12, packedQuantity = 11) }
        val connection = Connection(URL("https://example.test/v1/packing-image"), response(12))
        val service = RemotePackingImageService("https://example.test", openConnection = { connection })
        try {
            service.generate(PackingPlan.from(packing.activeBag), AppLanguage.English)
            fail("Mismatched inventory must be rejected")
        } catch (error: ImageServiceException) {
            assertEquals(ImageServiceProblem.Generation, error.problem)
        }
    }
}
