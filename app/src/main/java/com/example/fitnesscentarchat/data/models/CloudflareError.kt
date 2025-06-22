package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.time.LocalDateTime

data class CloudflareError(
    val code: Int,
    val message: String
)