package com.example.fitnesscentarchat.ui.screens.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MembershipViewModel(
    private val membershipRepository: MembershipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MembershipUiState())
    val uiState: StateFlow<MembershipUiState> = _uiState.asStateFlow()



    fun loadMembership(fitnessCenterId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            membershipRepository.GetUserMembershipByFitnessCenter(fitnessCenterId).fold(
                onSuccess = { membership ->
                    _uiState.update { it.copy(membership = membership, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load membership",
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