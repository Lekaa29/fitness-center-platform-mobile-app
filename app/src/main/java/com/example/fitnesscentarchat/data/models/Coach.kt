package com.example.fitnesscentarchat.data.models

data class Coach(
    val user: User,
    val description: String,
    val bannerPictureLink: String? = null,
    val programs: List<Program> = emptyList()
)