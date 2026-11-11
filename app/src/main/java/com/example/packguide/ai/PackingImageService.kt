package com.example.packguide.ai

import android.graphics.Bitmap
import com.example.packguide.settings.AppLanguage

/** A remote image generator. Provider credentials belong exclusively on the server. */
fun interface PackingImageService {
    suspend fun generate(plan: PackingPlan, language: AppLanguage): Bitmap
    suspend fun assess(photo: Bitmap, language: AppLanguage): PackingPhotoAssessment =
        throw ImageServiceException(ImageServiceProblem.Unavailable)
    val isAvailable: Boolean get() = true
}

data class PackingPhotoAssessment(val score: Int?, val feedback: String)
