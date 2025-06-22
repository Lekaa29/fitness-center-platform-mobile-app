package com.example.fitnesscentarchat.data.repository

import com.example.fitnesscentarchat.data.models.CloudflareImageResult
import com.example.fitnesscentarchat.data.models.CloudflareImagesResponse
import com.example.fitnesscentarchat.data.models.ImageUrls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.google.gson.Gson

class CloudflareUploader{

    companion object {
    // Replace with your actual Cloudflare credentials
    private const val ACCOUNT_ID = "21c3ac2bd9a6027ed060cc1377b0c891"
    private const val API_TOKEN = "FXS1NowLJ_6DyPGJ-601bgFNH0ImnmJcwumH4ZTX"
    private const val BASE_URL = "https://api.cloudflare.com/client/v4/accounts/$ACCOUNT_ID/images/v1"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    /**
     * Upload image file to Cloudflare Images
     */
    suspend fun uploadImage(
        imageFile: File,
        metadata: Map<String, String> = emptyMap()
    ): Result<CloudflareImageResult> = withContext(Dispatchers.IO) {
        try {
            // Validate file
            if (!imageFile.exists()) {
                return@withContext Result.failure(Exception("Image file does not exist"))
            }

            if (imageFile.length() > 10 * 1024 * 1024) { // 10MB limit
                return@withContext Result.failure(Exception("Image file too large (max 10MB)"))
            }

            // Determine media type
            val mediaType = getMediaType(imageFile)

            // Build multipart request
            val requestBodyBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    imageFile.name,
                    imageFile.asRequestBody(mediaType)
                )

            // Add metadata if provided
            metadata.forEach { (key, value) ->
                requestBodyBuilder.addFormDataPart(key, value)
            }

            val requestBody = requestBodyBuilder.build()

            // Create request
            val request = Request.Builder()
                .url(BASE_URL)
                .header("Authorization", "Bearer $API_TOKEN")
                .post(requestBody)
                .build()

            // Execute request
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (responseBody != null) {
                val cloudflareResponse = gson.fromJson(responseBody, CloudflareImagesResponse::class.java)

                if (cloudflareResponse.success && cloudflareResponse.result != null) {
                    Result.success(cloudflareResponse.result)
                } else {
                    val errorMessage = cloudflareResponse.errors?.firstOrNull()?.message
                        ?: "Unknown Cloudflare error"
                    Result.failure(Exception("Cloudflare upload failed: $errorMessage"))
                }
            } else {
                Result.failure(Exception("Empty response from Cloudflare"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get image variants URLs
     */
    fun getImageUrls(imageId: String): ImageUrls {
        val baseUrl = "https://imagedelivery.net/$ACCOUNT_ID/$imageId"
        return ImageUrls(
            original = "$baseUrl/public",
            thumbnail = "$baseUrl/thumbnail",
            preview = "$baseUrl/preview",
            avatar = "$baseUrl/avatar"
        )
    }

    /**
     * Delete image from Cloudflare
     */
    suspend fun deleteImage(imageId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/$imageId")
                .header("Authorization", "Bearer $API_TOKEN")
                .delete()
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (responseBody != null) {
                val cloudflareResponse = gson.fromJson(responseBody, CloudflareImagesResponse::class.java)
                Result.success(cloudflareResponse.success)
            } else {
                Result.failure(Exception("Empty response from Cloudflare"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMediaType(file: File): MediaType {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg".toMediaType()
            "png" -> "image/png".toMediaType()
            "gif" -> "image/gif".toMediaType()
            "webp" -> "image/webp".toMediaType()
            "bmp" -> "image/bmp".toMediaType()
            else -> "image/jpeg".toMediaType() // default
        }
    }
}