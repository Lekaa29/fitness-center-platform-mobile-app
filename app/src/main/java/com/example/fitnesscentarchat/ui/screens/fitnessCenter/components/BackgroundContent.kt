package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterUiState
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterViewModel
import com.example.fitnesscentarchat.ui.screens.shop.components.BuyItemOverlay


@Composable
fun FitnessCenterContent(
    currentUser: User?,
    onChatClicked: (Int, Int, String) -> Unit,
    uiState: FitnessCenterUiState,
    scrollState: androidx.compose.foundation.ScrollState,
    onTopTextPositioned: (Float) -> Unit,
    onShowNewsOverlayChange: (Boolean) -> Unit,
    showNewsOverlay:Boolean,
    onLeaderboardOverlayChange: (Boolean) -> Unit,
    showLeaderboardOverlay:Boolean,
    onCoachesOverlayChange: (Boolean) -> Unit,
    showCoachesOverlay:Boolean,
    onshowGraphOverlayChange: (Boolean) -> Unit,
    showGraphOverlay: Boolean,
    onViewQRChange: (Boolean) -> Unit,
    viewQROverlay: Boolean,
    viewModel: FitnessCenterViewModel,
    onUserChatSelect: () -> Unit,
    onUserItemsSelect: () -> Unit
) {
    val animatedColor = rememberAnimatedGradientColor(uiState.fitnessCenter?.color ?: "0xFF000000")

    Log.d("center array", "${uiState.coaches}")
    Log.d("center array", "${uiState.news}")
    Log.d("center array", "${uiState.allAttendances}")
    Log.d("center array", "${uiState.leaderboard}")

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            animatedColor,
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                            Color(0xFF000000),
                        )
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackgroundTopBar(
                showNewsOverlay,
                currentUser,
                onUserChatSelect=onUserChatSelect,
                onUserItemsSelect=onUserItemsSelect
            )
            Spacer(modifier = Modifier.padding(20.dp))
            TopTextSection(onTopTextPositioned = onTopTextPositioned, onViewGraphClick = { onshowGraphOverlayChange(true) }, uiState.recentAttendance)
            TopActionsContainer(uiState.soonLeaving, onViewQrClick = { onViewQRChange(true) })
            Spacer(modifier = Modifier.padding(12.dp))
            Line(Color.White)
            Spacer(modifier = Modifier.padding(4.dp))

            // Pass the callback to show overlay
            NewsSection(
                newsList = uiState.news,
                onShowAllClick = { onShowNewsOverlayChange(true) }
            )

            Spacer(modifier = Modifier.padding(4.dp))
            CoachesSection(
                coaches = uiState.coaches,
                onShowPrograms = { onCoachesOverlayChange(true) }
            )
            SimpleLeaderboard(users = uiState.leaderboard,
                onViewTableClick = { onLeaderboardOverlayChange(true) },
                detail = false)

            Spacer(modifier = Modifier.padding(20.dp))

        }

        // Overlay
        AnimatedVisibility(
            visible = showNewsOverlay,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            NewsDetailOverlay(
                newsItems = uiState.news,
                onBackClick = { onShowNewsOverlayChange(false) }
            )
        }

        AnimatedVisibility(
            visible = viewQROverlay,
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
            viewQROverlay(
                qr = "",
                onBackClick = {
                    onViewQRChange(false)
                }
            )
        }

        AnimatedVisibility(
            visible = showLeaderboardOverlay,
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
            LeaderboardOverlay(
                rankedUser = uiState.leaderboard,
                onBackClick = { onLeaderboardOverlayChange(false) }
            )
        }

        AnimatedVisibility(
            visible = showCoachesOverlay,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            CoachesOverlay(
                coaches = uiState.coaches,
                onBackClick = { onCoachesOverlayChange(false) },
                onChatClicked = onChatClicked,
                viewModel = viewModel
            )
        }

        AnimatedVisibility(
            visible = showGraphOverlay,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            DateRangeAnalyzer(
                uiState.allAttendances,
                onBackClick = { onshowGraphOverlayChange(false) }
            )

        }

    }
}





@Composable
fun TopTextSection(onTopTextPositioned: (Float) -> Unit, onViewGraphClick: () -> Unit, recentAttendance: Int?) {
    val newRecentAttendance = recentAttendance ?: 0
    Row(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .onGloballyPositioned {
                onTopTextPositioned(it.positionInParent().y)
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Live attendance", color = Color(0xF2BDBDBD), fontSize=12.sp, fontWeight= FontWeight.Light)
            Text(text = newRecentAttendance.toString(), color = Color.White, fontSize=84.sp)

            transparentButton(text = "View activity", onViewGraphClick)

        }
    }
}


