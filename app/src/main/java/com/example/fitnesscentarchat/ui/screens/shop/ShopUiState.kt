package com.example.fitnesscentarchat.ui.screens.hub

import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.ShopItem

data class ShopUiState(
    val isLoading: Boolean = false,
    val shopItems: List<ShopItem> = emptyList(),
    val membership: MembershipModel? = null,
    //val coaches
    //val membershipPackages
    val fitnessCenter: FitnessCenter? = null,

    val error: String? = null,
)