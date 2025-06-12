package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.Membership

data class MembershipUiState(
    val isLoading: Boolean = false,
    val membership: Membership? = null,
    //val coaches
    //val membershipPackages
    val error: String? = null,
)