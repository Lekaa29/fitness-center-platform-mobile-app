package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.FitnessCenter

data class HubUiState(
    val isLoading: Boolean = false,
    val fitnessCenters: List<FitnessCenter> = emptyList(),
    val error: String? = null,
    val currentUser: User? = null,
    val usersMemberships: List<MembershipModel> = emptyList(),
    val promoFitnessCentars: List<FitnessCenter> = emptyList(),
    val nearFitnessCentars: List<FitnessCenter> = emptyList(),
    val isFromCache: Boolean = false, // Indicates if current data is from cache
    val isRefreshing: Boolean = false, // For pull-to-refresh scenarios
    val lastUpdated: Long = 0L // Timestamp of last successful update
)