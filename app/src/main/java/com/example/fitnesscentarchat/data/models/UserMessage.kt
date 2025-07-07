package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class UserMessage(
    val messageId: Int,
    val userId: Int,
    val isRead: Boolean,
    val name: String
)