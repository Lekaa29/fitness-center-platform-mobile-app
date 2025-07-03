package com.example.fitnesscentarchat.ui.screens.profile.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesscentarchat.ui.screens.profile.ProfileUiState

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    scrollState: androidx.compose.foundation.ScrollState,
    onTopTextPositioned: (Float) -> Unit,
    onEditProfileChange: (Boolean) -> Unit,
    editProfileOverlay: Boolean,
    imagePickerLauncher : ManagedActivityResultLauncher<String, Uri?>,
    isUploading :Boolean,
    uploadError :String?
) {


    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xF00C1813),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                        )
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            TopTextSection(user=uiState.user,onTopTextPositioned = onTopTextPositioned, onEditProfileChange = { onEditProfileChange(true)})
            Spacer(modifier = Modifier.padding(20.dp))

            MobileSelect(
                items = uiState.memberships,
                selectedIndex = selectedIndex,
                onSelectionChanged = { selectedIndex = it }
            )
            Spacer(modifier = Modifier.padding(20.dp))

            StreakBoxes(uiState.memberships[selectedIndex].streakRunCount.toString(), uiState.memberships[selectedIndex].points.toString())

            AttendanceCalendarGrid(attendances = uiState.attendances)

        }
        AnimatedVisibility(
            visible = editProfileOverlay,
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
            EditUserOverlay(
                user = uiState.user,
                onBackClick = { onEditProfileChange(false) },
                imagePickerLauncher,
                isUploading

            )
        }


    }
}