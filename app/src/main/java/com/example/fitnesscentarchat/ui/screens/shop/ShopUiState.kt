package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.ShopItem

data class ShopUiState(
    val isLoading: Boolean = false,
    val shopItems: List<ShopItem> = emptyList(),
    //val coaches
    //val membershipPackages
    val error: String? = null,
)