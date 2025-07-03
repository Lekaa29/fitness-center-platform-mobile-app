package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.AttendanceRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FitnessCenterViewModel(
    private val fitnessCenterRepository: FitnessCenterRepository,
    private val membershipRepository: MembershipRepository,
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FitnessCenterUiState())
    val uiState: StateFlow<FitnessCenterUiState> = _uiState.asStateFlow()



    fun loadFitnessCenterHome(fitnessCenterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            attendanceRepository.GetRecentAttendees(fitnessCenterId).fold(
                onSuccess = { recentAttendees ->
                    _uiState.update { it.copy(recentAttendance = recentAttendees, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load recent Attendance",
                            isLoading = false
                        )
                    }
                }
            )

            attendanceRepository.GetAllAttendences(fitnessCenterId).fold(
                onSuccess = { allAttendences ->
                    _uiState.update { it.copy(allAttendances = allAttendences, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load all Attendance",
                            isLoading = false
                        )
                    }
                }
            )

            fitnessCenterRepository.GetFitnessCentarsCoaches(fitnessCenterId).fold(
                onSuccess = { coaches ->
                    _uiState.update { it.copy(coaches = coaches, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load coaches",
                            isLoading = false
                        )
                    }
                }
            )

            membershipRepository.GetFitnessCenterLeaderboard(fitnessCenterId).fold(
                onSuccess = { memberships ->
                    _uiState.update { it.copy(leaderboard = memberships, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load leaderboard",
                            isLoading = false
                        )
                    }
                }
            )

            fitnessCenterRepository.GetFitnessCenterArticles(fitnessCenterId).fold(
                onSuccess = { articles ->
                    _uiState.update { it.copy(news = articles, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load news articles",
                            isLoading = false
                        )
                    }
                }
            )

            attendanceRepository.GetLeavingAttendees(fitnessCenterId).fold(
                onSuccess = { leavingAttendees ->
                    _uiState.update { it.copy(soonLeaving = leavingAttendees, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load leaving Attendance",
                            isLoading = false
                        )
                    }
                }
            )


            fitnessCenterRepository.GetFitnessCenter(fitnessCenterId).fold(
                onSuccess = { fitnessCenter ->
                    _uiState.update { it.copy(fitnessCenter = fitnessCenter, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load fitness center",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}