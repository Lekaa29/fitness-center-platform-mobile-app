package com.example.fitnesscentarchat.ui.screens.map.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapContent(
    gyms: List<FitnessCenter>,
    selectedGymFromTable: FitnessCenter? = null, // Add parameter for externally selected gym
    modifier: Modifier = Modifier
) {
    var selectedGym by remember { mutableStateOf<FitnessCenter?>(null) }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    val location = LatLng(gyms.first().latitude, gyms.first().longitude)

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            location,
            12f
        )
    }

    // Update selected gym when external selection changes
    LaunchedEffect(selectedGymFromTable) {
        selectedGymFromTable?.let { gym ->
            selectedGym = gym
            // Animate camera to the selected gym
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(gym.latitude, gym.longitude), 15f),
                durationMs = 1000
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings.copy(
                zoomControlsEnabled = true,
                compassEnabled = true,
                myLocationButtonEnabled = true,
                mapToolbarEnabled = false
            )
        ) {
            // Add markers for each gym
            gyms.forEach { gym ->
                Marker(
                    state = MarkerState(position = LatLng(gym.latitude, gym.longitude)),
                    title = gym.name,
                    onClick = { marker ->
                        selectedGym = gym
                        true // Consume the click event
                    }
                )
            }
        }

        // Bottom sheet for selected gym
        selectedGym?.let { gym ->
            GymBottomSheet(
                gym = gym,
                onDismiss = { selectedGym = null },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
























