package com.example.packguide.scanning

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoScanner {
    suspend fun decode(context: Context, uri: Uri): Bitmap {
        var decoded: Bitmap? = null
        try {
            return withContext(Dispatchers.IO) {
                val resolver = context.contentResolver
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
                require(bounds.outWidth > 0 && bounds.outHeight > 0) { "Invalid image" }
                var sample = 1
                while (maxOf(bounds.outWidth, bounds.outHeight) / sample > 1600) sample *= 2
                val options = BitmapFactory.Options().apply { inSampleSize = sample }
                val bitmap = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
                    ?: error("Invalid image")
                try {
                    val exif = resolver.openInputStream(uri)?.use { ExifInterface(it) }
                    val matrix = Matrix().apply {
                        if (exif?.isFlipped == true) postScale(-1f, 1f)
                        postRotate((exif?.rotationDegrees ?: 0).toFloat())
                    }
                    val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    if (rotated !== bitmap) bitmap.recycle()
                    decoded = rotated
                    rotated
                } catch (error: Exception) {
                    bitmap.recycle()
                    throw error
                }
            }
        } catch (error: Exception) {
            // withContext can discard its result when the owning ViewModel is canceled.
            decoded?.recycle()
            throw error
        }
    }

}
