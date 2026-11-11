package com.example.packguide.ai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.packguide.BuildConfig
import com.example.packguide.settings.AppLanguage
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URI
import java.net.URL

enum class ImageServiceProblem { Unavailable, NotConfigured, LocalConnection, ProviderAccess, ProviderQuota, Timeout, Busy, Generation }
class ImageServiceException(val problem: ImageServiceProblem) : IOException()

class RemotePackingImageService(
    private val baseUrl: String = BuildConfig.PACKING_AI_BASE_URL,
    private val accessTokenProvider: () -> String? = { null },
    private val openConnection: (URL) -> HttpURLConnection = { it.openConnection() as HttpURLConnection }
) : PackingImageService {
    override val isAvailable: Boolean get() = runCatching { endpoint() }.isSuccess

    private fun endpoint(path: String = "/v1/packing-image"): URL {
        val uri = runCatching { URI(baseUrl.trim().trimEnd('/')) }.getOrNull()
        val localDebug = BuildConfig.DEBUG && uri?.scheme == "http" && uri.host in setOf("localhost", "127.0.0.1", "10.0.2.2")
        if (uri == null || uri.host.isNullOrBlank() || uri.userInfo != null || uri.query != null || uri.fragment != null ||
            (uri.scheme != "https" && !localDebug)) throw ImageServiceException(ImageServiceProblem.Unavailable)
        return URL(uri.toString() + path)
    }

    override suspend fun generate(plan: PackingPlan, language: AppLanguage): Bitmap {
        require(plan.contents.isNotEmpty())
        var decoded: Bitmap? = null
        try {
            return exchange("/v1/packing-image", 14_000_000, { createRequest(plan, language) }) { response ->
                verifyManifest(plan, response)
                if (response.getString("mimeType") != "image/jpeg") throw ImageServiceException(ImageServiceProblem.Generation)
                val bytes = Base64.decode(response.getString("imageBase64"), Base64.DEFAULT)
                if (bytes.size > 10_000_000) throw ImageServiceException(ImageServiceProblem.Generation)
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
                if (bounds.outWidth !in 1..2048 || bounds.outHeight !in 1..2048 ||
                    bounds.outWidth.toLong() * bounds.outHeight > 4_194_304) {
                    throw ImageServiceException(ImageServiceProblem.Generation)
                }
                val result = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    ?: throw ImageServiceException(ImageServiceProblem.Generation)
                decoded = result
                result
            }
        } catch (error: Throwable) {
            decoded?.recycle()
            throw error
        }
    }

    override suspend fun assess(photo: Bitmap, language: AppLanguage): PackingPhotoAssessment =
        exchange("/v1/packing-review", 16_000, {
            val bytes = ByteArrayOutputStream().use { stream ->
                if (!photo.compress(Bitmap.CompressFormat.JPEG, 82, stream) || stream.size() > 3_000_000) {
                    throw ImageServiceException(ImageServiceProblem.Generation)
                }
                stream.toByteArray()
            }
            JSONObject().put("schemaVersion", 1).put("language", language.storageKey)
                .put("photoBase64", Base64.encodeToString(bytes, Base64.NO_WRAP))
        }) { response ->
            val score = if (response.isNull("score")) null else response.getInt("score")
            val feedback = response.getString("feedback").trim()
            if (response.getInt("schemaVersion") != 1 || !response.has("score") ||
                (score != null && score !in 1..10) || feedback.isEmpty() || feedback.length > 1200) {
                throw ImageServiceException(ImageServiceProblem.Generation)
            }
            PackingPhotoAssessment(score, feedback)
        }

    private suspend fun <T> exchange(
        path: String, responseLimit: Int, payload: () -> JSONObject, decode: (JSONObject) -> T
    ): T {
        val url = endpoint(path)
        try {
            return withContext(Dispatchers.IO) {
                val request = payload().toString().toByteArray(Charsets.UTF_8)
                currentCoroutineContext().ensureActive()
                coroutineScope {
                    val connection = openConnection(url)
                    // Disconnect the blocking socket on cancellation, not just after a read timeout.
                    val cancellation = launch(start = CoroutineStart.UNDISPATCHED) {
                        try { awaitCancellation() } finally { connection.disconnect() }
                    }
                    try {
                        connection.requestMethod = "POST"
                        connection.instanceFollowRedirects = false
                        connection.connectTimeout = 15_000
                        connection.readTimeout = 210_000
                        connection.doOutput = true
                        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                        connection.setRequestProperty("Accept", "application/json")
                        accessTokenProvider()?.takeIf { it.isNotBlank() }?.let {
                            connection.setRequestProperty("Authorization", "Bearer $it")
                        }
                        connection.setFixedLengthStreamingMode(request.size)
                        connection.outputStream.use { it.write(request) }
                        val status = connection.responseCode
                        if (status !in 200..299) {
                            val code = runCatching {
                                connection.errorStream?.use { JSONObject(readBounded(it, 64_000).toString(Charsets.UTF_8)) }
                                    ?.optJSONObject("error")?.optString("code")
                            }.getOrNull()
                            throw ImageServiceException(when {
                                code == "NOT_CONFIGURED" -> ImageServiceProblem.NotConfigured
                                code == "PROVIDER_ACCESS" -> ImageServiceProblem.ProviderAccess
                                code == "PROVIDER_QUOTA" -> ImageServiceProblem.ProviderQuota
                                code == "GENERATION_TIMEOUT" -> ImageServiceProblem.Timeout
                                status == 401 || status == 403 -> ImageServiceProblem.Unavailable
                                status == 429 -> ImageServiceProblem.Busy
                                else -> ImageServiceProblem.Generation
                            })
                        }
                        val response = connection.inputStream.use {
                            JSONObject(readBounded(it, responseLimit).toString(Charsets.UTF_8))
                        }
                        currentCoroutineContext().ensureActive()
                        decode(response)
                    } finally { cancellation.cancel(); connection.disconnect() }
                }
            }
        } catch (error: Throwable) {
            currentCoroutineContext().ensureActive()
            when {
                error is ImageServiceException -> throw error
                error is SocketTimeoutException -> throw ImageServiceException(ImageServiceProblem.Timeout)
                error is IOException && url.protocol == "http" -> throw ImageServiceException(ImageServiceProblem.LocalConnection)
                else -> throw error
            }
        }
    }

    internal fun createRequest(plan: PackingPlan, language: AppLanguage): JSONObject {
        val items = JSONArray()
        plan.contents.forEach { item ->
            items.put(JSONObject().put("id", item.id).put("packedQuantity", item.quantity).put("zone", item.zone))
        }
        val request = JSONObject().put("schemaVersion", 1).put("language", language.storageKey)
            .put("dimensions", JSONArray(plan.dimensions)).put("items", items)
        return request
    }

    private fun verifyManifest(plan: PackingPlan, response: JSONObject) {
        if (response.getInt("schemaVersion") != 1) throw ImageServiceException(ImageServiceProblem.Generation)
        val manifest = response.getJSONArray("packedItems")
        val expected = plan.contents.associate { it.id to it.quantity }
        val received = mutableMapOf<String, Int>()
        for (index in 0 until manifest.length()) {
            val item = manifest.getJSONObject(index)
            val id = item.getString("id")
            if (received.put(id, item.getInt("packedQuantity")) != null) throw ImageServiceException(ImageServiceProblem.Generation)
        }
        if (received != expected) throw ImageServiceException(ImageServiceProblem.Generation)
    }

    private fun readBounded(input: InputStream, limit: Int): ByteArray {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            if (output.size().toLong() + count > limit) throw ImageServiceException(ImageServiceProblem.Generation)
            output.write(buffer, 0, count)
        }
        return output.toByteArray()
    }
}
