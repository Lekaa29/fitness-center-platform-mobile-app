package com.example.fitnesscentarchat.ui.screens.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel(
    private val fitnessCenterRepository: FitnessCenterRepository
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
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}