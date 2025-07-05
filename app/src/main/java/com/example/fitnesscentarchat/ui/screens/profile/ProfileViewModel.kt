package com.example.fitnesscentarchat.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.repository.AttendanceRepository
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.ShopRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val membershipRepository: MembershipRepository,
    private val attendanceRepository: AttendanceRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()




    fun loadProfile(fitnessCenterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            Log.d("ProfileViewModel", "Starting to load profile for fitness center: $fitnessCenterId")

            try {
                // Load attendances
                attendanceRepository.GetCurrentUserFitnessCenterAttendances(fitnessCenterId).fold(
                    onSuccess = { attendances ->
                        Log.d("ProfileViewModel", "Successfully loaded ${attendances.size} attendances")
                        attendances.forEach { attendance ->
                            Log.d("ProfileViewModel", "Attendance: ID=${attendance.idAttendance}, Timestamp=${attendance.timestamp}")
                        }
                        _uiState.update {
                            it.copy(
                                attendances = attendances,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.e("ProfileViewModel", "Failed to load attendances: ${error.message}", error)
                        _uiState.update {
                            it.copy(
                                error = error.message ?: "Failed to load attendances",
                                isLoading = false
                            )
                        }
                    }
                )

                membershipRepository.GetCurrentUserMemberships().fold(
                    onSuccess = { memberships ->
                        Log.d("ProfileViewModel", "Successfully loaded ${memberships.size} memberships")

                        _uiState.update {
                            it.copy(
                                memberships = memberships,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.e("ProfileViewModel", "Failed to load memberships: ${error.message}", error)
                        _uiState.update {
                            it.copy(
                                error = error.message ?: "Failed to load memberships",
                                isLoading = false
                            )
                        }
                    }
                )

                // Load user info
                val currentUser = authRepository.getCurrentUser()
                Log.d("ProfileViewModel", "Current user: ${currentUser?.firstName}")
                _uiState.update {
                    it.copy(
                        user = currentUser
                    )
                }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Unexpected error in loadProfile", e)
                _uiState.update {
                    it.copy(
                        error = "Unexpected error: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }



    fun updateUser(imageUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)

            userRepository.UpdateUser(imageUrl).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isUploading = false)
                },
                onFailure = { error ->
                    Log.e("VIEWMODEL", "Failed to send message", error)
                    _uiState.value = _uiState.value.copy(
                        isUploading = false,
                        error = "Failed to send: ${error.message}"
                    )
                }
            )
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}