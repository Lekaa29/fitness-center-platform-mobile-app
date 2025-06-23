package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Coach
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.MembershipModel


data class FitnessCenterUiState(
    val isLoading: Boolean = false,
    val fitnessCenter: FitnessCenter? = null,
    val allAttendance: List<Attendance> = emptyList(),
    val recentAttendance: Int? = null,
    val soonLeaving: Int? = null,
    val error: String? = null,
    val news: List<Article> = emptyList(),
    val coaches: List<Coach> = emptyList(),
    val leaderboard: List<MembershipModel> = emptyList()
)
