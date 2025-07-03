package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.MembershipPackage
import com.example.fitnesscentarchat.data.models.User

data class MembershipUiState(
    val isLoading: Boolean = false,
    val membershipPackages: List<MembershipPackage>? = emptyList(),
    val user: User? = null,
    val membership: MembershipModel? = null,
    val error: String? = null,
)



