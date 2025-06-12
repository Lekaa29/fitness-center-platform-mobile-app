package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import com.example.fitnesscentarchat.data.models.FitnessCenter

data class FitnessCenterUiState(
    val isLoading: Boolean = false,
    val fitnessCenter: FitnessCenter? = null,
    val recentAttendance: Int? = null,
    val error: String? = null,
)