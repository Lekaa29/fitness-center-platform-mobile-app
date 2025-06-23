package com.example.fitnesscentarchat.ui.screens.profile.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StreakBoxes(streakNumber: String, pointsNumber: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Streak Box with border
        Box(
            modifier = Modifier
                .height(124.dp) // 120 + 2dp top + 2dp bottom
                .width(144.dp)  // 140 + 2dp left + 2dp right
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0x2AFF8400),
                            Color(0xFFFF8400),
                            Color(0xFFFF8400),
                        )
                    ), // your desired border color
                    shape = RoundedCornerShape(18.dp) // slightly more rounded
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .height(120.dp)
                    .width(140.dp)
                    .background(
                        Color(0xFF0F0F0F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedContent(
                    targetState = streakNumber,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1500)) togetherWith
                                fadeOut(animationSpec = tween(150))
                    },
                    label = "streak_animation"
                ) { animatedStreakNumber ->
                    Text(
                        text = animatedStreakNumber,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                Text(
                    text = "streak",
                    fontSize = 12.sp,
                    color = Color(0xFF747474),
                )
            }
        }

        // Points Box with border
        Box(
            modifier = Modifier
                .height(124.dp)
                .width(144.dp)
                .background(
                    color = Color(0xFF333333),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .height(120.dp)
                    .width(140.dp)
                    .background(
                        Color(0xFF0F0F0F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedContent(
                    targetState = pointsNumber,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1000)) togetherWith
                                fadeOut(animationSpec = tween(150))
                    },
                    label = "points_animation"
                ) { animatedPointsNumber ->
                    Text(
                        text = "27$animatedPointsNumber",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                Text(
                    text = "points",
                    fontSize = 12.sp,
                    color = Color(0xFF747474),
                )
            }
        }
    }
}






