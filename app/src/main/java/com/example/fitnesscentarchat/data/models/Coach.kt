package com.example.fitnesscentarchat.data.models

import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterViewModel

data class Coach(
    val user: User,
    val idCoach: Int,
    val description: String,
    val bannerPictureLink: String,
    val programs: List<Program> = emptyList()
)