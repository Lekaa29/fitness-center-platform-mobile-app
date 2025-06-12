package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class Message(
    @Json(name = "idMessage") val id: Int,
    @Json(name = "idSender") val senderId: Int,
    @Json(name = "text") val text: String,
    @Json(name = "timestamp") val timestamp: String?,
    @Json(name = "isDeleted") val isDeleted: Boolean,
    @Json(name = "isPinned") val isPinned: Boolean = false,
    @Json(name = "idConversation") val idConversation: Int

)