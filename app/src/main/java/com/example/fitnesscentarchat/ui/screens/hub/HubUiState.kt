package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.FitnessCenter

data class HubUiState(
    val isLoading: Boolean = false,
    val fitnessCenters: List<FitnessCenter> = emptyList(),
    val error: String? = null,
)