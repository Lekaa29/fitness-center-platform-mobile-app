package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

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


@Composable
fun BackgroundScrollableContent(
    currentUser: User,
    uiState: FitnessCenterUiState,
    onTopTextPositioned: (Float) -> Unit,
    onShowNewsOverlayChange: (Boolean) -> Unit,
    showNewsOverlay:Boolean,
    onLeaderboardOverlayChange: (Boolean) -> Unit,
    showLeaderboardOverlay:Boolean,
    onCoachesOverlayChange: (Boolean) -> Unit,
    showCoachesOverlay:Boolean,
    onshowGraphOverlayChange: (Boolean) -> Unit,
    showGraphOverlay: Boolean
) {
    val animatedColor = rememberAnimatedGradientColor()

    val newsItems = listOf(
        mapOf("title" to "Ne radimo 17.6", "imageUrl" to "https://scontent.fzag4-1.fna.fbcdn.net/v/t1.6435-9/96379603_159721362199244_1576151752367931392_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=833d8c&_nc_ohc=sQAuHjV-qSIQ7kNvwE9QuQA&_nc_oc=Adk_8r6iSXlg34M4EWUX2HSGwZ6DoW_wHCggcGy_3rCFvu4aG2mRDaxCanvbvSqzoLQ&_nc_zt=23&_nc_ht=scontent.fzag4-1.fna&_nc_gid=tJ4_peV25w8GbDfBi7uThg&oh=00_AfORP5uxJN24xOtS9UQFGyT_yId7q34aFsPhN8DEXLlwEA&oe=687959D0"),
        mapOf("title" to "Pravila", "imageUrl" to "https://scontent.fzag4-1.fna.fbcdn.net/v/t1.6435-9/96379603_159721362199244_1576151752367931392_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=833d8c&_nc_ohc=sQAuHjV-qSIQ7kNvwE9QuQA&_nc_oc=Adk_8r6iSXlg34M4EWUX2HSGwZ6DoW_wHCggcGy_3rCFvu4aG2mRDaxCanvbvSqzoLQ&_nc_zt=23&_nc_ht=scontent.fzag4-1.fna&_nc_gid=tJ4_peV25w8GbDfBi7uThg&oh=00_AfORP5uxJN24xOtS9UQFGyT_yId7q34aFsPhN8DEXLlwEA&oe=687959D0"),
        mapOf("title" to "Nova smith sprava", "imageUrl" to "https://scontent.fzag4-1.fna.fbcdn.net/v/t1.6435-9/96379603_159721362199244_1576151752367931392_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=833d8c&_nc_ohc=sQAuHjV-qSIQ7kNvwE9QuQA&_nc_oc=Adk_8r6iSXlg34M4EWUX2HSGwZ6DoW_wHCggcGy_3rCFvu4aG2mRDaxCanvbvSqzoLQ&_nc_zt=23&_nc_ht=scontent.fzag4-1.fna&_nc_gid=tJ4_peV25w8GbDfBi7uThg&oh=00_AfORP5uxJN24xOtS9UQFGyT_yId7q34aFsPhN8DEXLlwEA&oe=687959D0")
    )

    val sampleUsers = listOf(
        mapOf("username" to "Lekaa29", "points" to "1500"),
        mapOf("username" to "benediktiner", "points" to "1200"),
        mapOf("username" to "agavaga", "points" to "1100"),
        mapOf("username" to "luleCR7", "points" to "950"),
        mapOf("username" to "spaja2", "points" to "800"),
        mapOf("username" to "sancho17", "points" to "750"),
        mapOf("username" to "radja", "points" to "700"),
        mapOf("username" to "ega00", "points" to "650"),
        mapOf("username" to "anaa11", "points" to "600"),
        mapOf("username" to "nikol22", "points" to "550")
    )

    val coaches = listOf(
        mapOf("name" to "Anton Parat", "price" to "12", "url" to "https://scontent.fzag4-1.fna.fbcdn.net/v/t39.30808-6/457669530_1031801975615405_5482562526053612964_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=gvsFDXJEwr0Q7kNvwFxPZRv&_nc_oc=AdkI9wdlHu6O_NY5t1k73e9dvV3xu4Agm-_q8Z9SqGo_PYW1soSZh_aO1dCZZWMbOCs&_nc_zt=23&_nc_ht=scontent.fzag4-1.fna&_nc_gid=P3ER2tylAL2l5_UIIXHMOw&oh=00_AfPPXrxwWEfoYIIDB39GD6p7Tk_5-PeqwJKZNZnlwEagnA&oe=6858E4ED", "programCount" to "5", "description" to "Fokus na treninge za muškarce, razvoj mišićne mase, mršavljenje, oporavak od ozljeda, pripreme za specifične sportove..."),
        mapOf("name" to "Filip Zdep", "price" to "15", "url" to "https://scontent.fzag1-2.fna.fbcdn.net/v/t39.30808-6/493212790_1227841952678072_3967906942302841225_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=833d8c&_nc_ohc=F0SIXpuopIAQ7kNvwFAUYSm&_nc_oc=AdmsQ98l-RUCY0Rbhr7HGKYE7YOYsLJ8fuCIwEqQkTqBiYmeqQxNlUSXIVkQsi1JLbc&_nc_zt=23&_nc_ht=scontent.fzag1-2.fna&_nc_gid=vYyql7YJmewUZTDYAptjTw&oh=00_AfM60sm4iwlNcBEpAHZ6JzEEVAJj5oDDavcowl9XeCxpTA&oe=6858E0D3", "programCount" to "7", "description" to "Pokrivam veliku vrstu treninga za svaku dob, ciljeve, intenzitete i oporavke."),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                showNewsOverlay, currentUser
            )
            Spacer(modifier = Modifier.padding(20.dp))
            TopTextSection(onTopTextPositioned = onTopTextPositioned, onViewGraphClick = { onshowGraphOverlayChange(true) }, uiState.recentAttendance)
            TopActionsContainer(uiState.soonLeaving)
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
                onBackClick = { onCoachesOverlayChange(false) }
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
                uiState.allAttendance,
                onBackClick = { onshowGraphOverlayChange(false) }
            )

        }

    }
}





@Composable
fun TopTextSection(onTopTextPositioned: (Float) -> Unit, onViewGraphClick: () -> Unit, recentAttendance: Int?) {
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
            Text(text = recentAttendance.toString(), color = Color.White, fontSize=84.sp)

            transparentButton(text = "View activity", onViewGraphClick)

        }
    }
}


