package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json

data class Conversation (
    @Json(name = "idConversation") val IdConversation: Int,
    @Json(name = "title") val Title: String?,
    @Json(name = "isGroup") val IsGroup: Boolean,
    @Json(name = "isDeleted") val IsDeleted: Boolean,
    @Json(name = "unreadCount") val UnreadCount: Int,
    @Json(name = "imageUrl") val ImageUrl: String? = "",
    @Json(name = "groupOwnerId") val groupOwnerId: Int
)

