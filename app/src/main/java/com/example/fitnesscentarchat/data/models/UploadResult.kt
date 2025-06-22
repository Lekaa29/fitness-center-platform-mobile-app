package com.example.fitnesscentarchat.data.models

data class UploadResult(
    val imageId: String,
    val filename: String,
    val urls: ImageUrls,
    val uploadedAt: String
)