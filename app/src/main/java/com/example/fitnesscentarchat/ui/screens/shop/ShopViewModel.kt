package com.example.fitnesscentarchat.ui.screens.hub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShopViewModel(
    private val shopRepository: ShopRepository,
    private val membershipRepository: MembershipRepository,
    private val fitnessCenterRepository: FitnessCenterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()



    fun loadShopItems(){
        viewModelScope.launch {
            shopRepository.GetUserItems().fold(
                onSuccess = { userItems ->
                    Log.d("ShopViewModel", "Successfully received ${userItems.size} user items")
                    userItems.forEachIndexed { index, item ->
                        Log.d("ShopViewModel", "User Item $index: Name=${item.Name}, Price=${item.Price}, ID=${item.IdShopItem}")
                    }
                    _uiState.update { it.copy(userItems = userItems, isLoading = false) }
                },
                onFailure = { error ->
                    Log.e("ShopViewModel", "Failed to get user items: ${error.message}", error)
                    Log.e("ShopViewModel", "Error type: ${error.javaClass.simpleName}")


                    // Also log the stack trace for debugging
                    error.printStackTrace()
                }
            )
        }
    }

    fun loadShop(fitnessCenterId: Int) {
        Log.d("ShopViewModel", "loadShop called with fitnessCenterId: $fitnessCenterId")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            shopRepository.GetFitnessCenterItems(fitnessCenterId).fold(
                onSuccess = { shopItems ->
                    Log.d("ShopViewModel", "Successfully loaded ${shopItems.size} shop items")
                    _uiState.update { it.copy(shopItems = shopItems, isLoading = false) }
                },
                onFailure = { error ->
                    Log.e("ShopViewModel", "Failed to load shop items: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load items",
                            isLoading = false
                        )
                    }
                }
            )

            membershipRepository.GetUserMembershipByFitnessCenter(fitnessCenterId).fold(
                onSuccess = { membership ->
                    Log.d("ShopViewModel", "Successfully loaded membership: $membership")
                    _uiState.update { it.copy(membership = membership, isLoading = false) }
                },
                onFailure = { error ->
                    Log.e("ShopViewModel", "Failed to load membership: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load membership",
                            isLoading = false
                        )
                    }
                }
            )

            fitnessCenterRepository.GetFitnessCenter(fitnessCenterId).fold(
                onSuccess = { fitnessCenter ->
                    Log.d("ShopViewModel", "Successfully loaded fitness center: ${fitnessCenter.name}")
                    _uiState.update {
                        it.copy(
                            fitnessCenter = fitnessCenter,
                        )
                    }
                },
                onFailure = { error ->
                    Log.e("ShopViewModel", "Failed to load fitness center: ${error.message}", error)
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


    fun BuyShopItem(shopItemId: Int) {
        Log.d("ShopViewModel", "BuyShopItem called with ID: $shopItemId")

        viewModelScope.launch {
            try {
                shopRepository.BuyShopItem(shopItemId).fold(
                    onSuccess = {
                        Log.d("ShopViewModel", "Successfully bought item with ID: $shopItemId")
                        // Refresh user items after purchase
                        loadShop(uiState.value.fitnessCenter?.idFitnessCentar ?: 0)
                    },
                    onFailure = { error ->
                        Log.e("ShopViewModel", "Failed to buy item $shopItemId: ${error.message}", error)
                    }
                )
            } catch (e: Exception) {
                Log.e("ShopViewModel", "Exception in BuyShopItem: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        Log.d("ShopViewModel", "clearError called")
        _uiState.update { it.copy(error = null) }
    }

    // Add this method to manually trigger a refresh
    fun refreshUserItems() {
        Log.d("ShopViewModel", "refreshUserItems called")
        loadShopItems()
    }
}