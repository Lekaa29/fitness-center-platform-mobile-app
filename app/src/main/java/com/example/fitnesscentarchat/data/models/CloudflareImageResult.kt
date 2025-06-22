package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.time.LocalDateTime

data class CloudflareImageResult(
    val id: String,
    val filename: String,
    val uploaded: String,
    val requireSignedURLs: Boolean,
    val variants: List<String>
)