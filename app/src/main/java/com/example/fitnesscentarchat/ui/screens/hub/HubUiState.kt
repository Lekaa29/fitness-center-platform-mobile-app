package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.FitnessCenter

data class HubUiState(
    val isLoading: Boolean = false,
    val fitnessCenters: List<FitnessCenter> = emptyList(),
    val error: String? = null,
    val currentUser: User,
    val usersMemberships: List<MembershipModel> = emptyList(),
    val promoFitnessCentars: List<FitnessCenter> = emptyList(),
    val nearFitnessCentars: List<FitnessCenter> = emptyList()
)
