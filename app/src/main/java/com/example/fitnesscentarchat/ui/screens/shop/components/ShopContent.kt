package com.example.fitnesscentarchat.ui.screens.shop.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.dp
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.components.rememberAnimatedGradientColor
import com.example.fitnesscentarchat.ui.screens.hub.MembershipUiState
import com.example.fitnesscentarchat.ui.screens.hub.ShopUiState
import com.example.fitnesscentarchat.ui.screens.hub.ShopViewModel


@Composable
fun ShopContent(
    uiState: ShopUiState,
    scrollState: androidx.compose.foundation.ScrollState,
    onBuyItemChange: (Boolean) -> Unit,
    buyItemOverlay: Boolean,
    viewModel: ShopViewModel
) {

    // State to track the selected item
    var selectedItem by remember { mutableStateOf<ShopItem?>(null) }

    // Function to handle item selection
    val onItemSelected = { item: ShopItem ->
        selectedItem = item
        onBuyItemChange(true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFDD5025),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                        )
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            TopTextSection(fitnessCenterName=uiState.fitnessCenter?.name,userMembership = uiState.membership)
            Spacer(modifier = Modifier.padding(20.dp))
            Box(
                modifier = Modifier
                    .background(color = Color(0xBEFFFFFF))
                    .fillMaxWidth()
                    .height(2.dp),
            ) {

            }
            ShopItems(
                shopItems = uiState.shopItems,
                onItemSelected = onItemSelected
            )
        }

        // Overlay with proper item selection
        AnimatedVisibility(
            visible = buyItemOverlay,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            BuyItemOverlay(
                shopItem = selectedItem,
                onBackClick = {
                    selectedItem = null
                    onBuyItemChange(false)
                },
                viewModel=viewModel

            )
        }
    }
}