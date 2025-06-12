package com.example.fitnesscentarchat.ui.screens.profile

import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val attendances: List<Attendance> = emptyList(),
    //val coaches
    //val membershipPackages
    val error: String? = null,
)