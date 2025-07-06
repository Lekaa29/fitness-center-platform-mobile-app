package com.example.fitnesscentarchat.ui.screens.hub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MembershipViewModel(
    private val membershipRepository: MembershipRepository,
    private val authRepository: AuthRepository
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


            membershipRepository.GetFitnessCenterMembershipPackages(fitnessCenterId).fold(
                onSuccess = { membershipPackages ->
                    _uiState.update { it.copy(membershipPackages = membershipPackages, isLoading = false) }
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

            val currentUser = authRepository.getCurrentUser()
            _uiState.update {
                it.copy(
                    user = currentUser
                )
            }
        }
    }

    fun BuyMembership(fitnessCenterId: Int, membershipPackageId : Int)
    {
        Log.d("ShopViewModel", "BuyShopItem called with ID: $membershipPackageId")

        viewModelScope.launch {
            try {
                membershipRepository.AddMembership(fitnessCenterId, membershipPackageId).fold(
                    onSuccess = {
                        Log.d("ShopViewModel", "Successfully bought item with ID: $membershipPackageId")
                        // Refresh user items after purchase
                    },
                    onFailure = { error ->
                        Log.e("ShopViewModel", "Failed to buy item $membershipPackageId: ${error.message}", error)
                    }
                )
            } catch (e: Exception) {
                Log.e("ShopViewModel", "Exception in BuyShopItem: ${e.message}", e)
            }
        }
    }




    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}