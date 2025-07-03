package com.example.fitnesscentarchat.ui.screens.hub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IFitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMembershipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel(
    private val fitnessCenterRepository: IFitnessCenterRepository,
    private val membershipRepository: IMembershipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HubUiState())
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    init {
        loadFitnessCenters()

        /*
        // Observe changes
        viewModelScope.launch {
            userRepository.getUsersFlow().collect { users ->
                _uiState.update { it.copy(users = users, isLoading = false) }
            }
        }

         */
    }

    fun loadFitnessCenters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            fitnessCenterRepository.GetFitnessCenters().fold(
                onSuccess = { fitnessCenters ->
                    _uiState.update { it.copy(fitnessCenters = fitnessCenters, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load fitness centers",
                            isLoading = false
                        )
                    }
                }
            )

            fitnessCenterRepository.GetPromoFitnessCenters().fold(
                onSuccess = { promoFitnessCentars ->
                    _uiState.update { it.copy(promoFitnessCentars = promoFitnessCentars, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load fitness centers",
                            isLoading = false
                        )
                    }
                }
            )

            fitnessCenterRepository.GetClosestFitnessCentars( 45.8, 15.97).fold(
                onSuccess = { closestFitnessCentars ->
                    _uiState.update { it.copy(nearFitnessCentars = closestFitnessCentars, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load fitness centers",
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
                            usersMemberships = memberships,
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
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}