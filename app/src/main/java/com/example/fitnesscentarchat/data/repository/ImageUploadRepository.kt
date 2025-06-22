package com.example.fitnesscentarchat.data.repository

import com.example.fitnesscentarchat.data.models.ImageUrls
import com.example.fitnesscentarchat.data.models.UploadProgress
import com.example.fitnesscentarchat.data.models.UploadResult
import java.io.File

class ImageUploadRepository {
    private val cloudflareUploader = CloudflareUploader()

    /**
     * Complete upload workflow with progress callbacks
     */
    suspend fun uploadImageWithCallback(
        imageFile: File,
        onProgress: (UploadProgress) -> Unit
    ): Result<UploadResult> {
        try {
            onProgress(UploadProgress.Starting)

            // Upload to Cloudflare
            onProgress(UploadProgress.Uploading(0))

            val result = cloudflareUploader.uploadImage(imageFile)

            return if (result.isSuccess) {
                val imageResult = result.getOrNull()!!
                val urls = cloudflareUploader.getImageUrls(imageResult.id)

                onProgress(UploadProgress.Complete)

                Result.success(
                    UploadResult(
                        imageId = imageResult.id,
                        filename = imageResult.filename,
                        urls = urls,
                        uploadedAt = imageResult.uploaded
                    )
                )
            } else {
                onProgress(UploadProgress.Failed(result.exceptionOrNull()?.message ?: "Upload failed"))
                result.map { UploadResult("", "", ImageUrls("", "", "", ""), "") }
            }

        } catch (e: Exception) {
            onProgress(UploadProgress.Failed(e.message ?: "Unknown error"))
            return Result.failure(e)
        }
    }
}