package com.example.fitnesscentarchat.ui.screens.membership.components

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
import com.example.fitnesscentarchat.ui.screens.hub.MembershipUiState

@Composable
fun BackgroundScrollableContent(
    uiState: MembershipUiState,
    onTopTextPositioned: (Float) -> Unit,
    onMembershipItemChange: (Boolean) -> Unit,
    membershipItemOverlay: Boolean
) {
    // State management
    var selectedIndex by remember { mutableIntStateOf(0) }


    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFD312E2D),
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
            TopTextSection(user=uiState.user, membership=uiState.membership, onTopTextPositioned = onTopTextPositioned)

            MembershipItems(membershipPackages=uiState.membershipPackages, onMembershipItemChange)


        }
        AnimatedVisibility(
            visible = membershipItemOverlay,
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
            MembershipItemOverlay(
                membershipPackage = uiState.membershipPackages[selectedIndex],
                onBackClick = { onMembershipItemChange(false) }
            )
        }


    }
}
