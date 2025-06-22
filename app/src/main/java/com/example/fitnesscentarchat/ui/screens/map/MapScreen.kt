package com.example.fitnesscentarchat.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.ui.screens.hub.HubUiState
import com.example.fitnesscentarchat.ui.screens.map.components.MapContent
import com.example.fitnesscentarchat.ui.screens.map.components.MapTopBar

@Composable
fun MapScreen(onBackClick: () -> Unit, selectedGymForMap: FitnessCenter?, uiState: HubUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        MapContent(
            modifier = Modifier.fillMaxSize(),
            selectedGymFromTable = selectedGymForMap,
            gyms = uiState.fitnessCenters
        )

        MapTopBar(
            onBackClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f), // Ensure it's above the map,
            currentUser = uiState.currentUser
        )
    }
}
