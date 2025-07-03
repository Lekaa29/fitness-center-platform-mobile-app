package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json

data class Article(
    val title: String,
    val text: String,
    val coverPictureLink: String?,
    val date: String?
)