package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.time.LocalDateTime

data class CloudflareImagesResponse(
    val success: Boolean,
    val errors: List<CloudflareError>?,
    val messages: List<String>?,
    val result: CloudflareImageResult?
)