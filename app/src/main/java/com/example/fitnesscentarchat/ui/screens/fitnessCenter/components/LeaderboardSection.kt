package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.MembershipModel


@Composable
fun LeaderboardItem(rank: Int, username: String, points: String, detail: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rank with special styling for top 3

                Box(
                    modifier = Modifier
                        .size(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rank.toString(),
                        color =
                        if(detail){
                            when (rank) {
                                1 -> Color(0xFFFFD700) // Gold
                                2 -> Color(0xFFC0C0C0) // Silver/Gray
                                3 -> Color(0xFFCD7F32) // Brown/Bronze
                                else -> Color.White
                            }}else{
                            Color(0xFFFFFFFF)
                        },                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    if(detail){
                        Text(
                            text = username,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        // Add emoji for top 3
                        if (rank <= 3) {
                            Text(
                                text = when (rank) {
                                    1 -> "👑"
                                    2 -> "🥈"
                                    3 -> "🥉"
                                    else -> ""
                                },
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                    else {
                        Text(
                            text = username,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = "$points pts",
                color = Color(0xFFFFFFFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun SimpleLeaderboard(users: List<MembershipModel>,
                      onViewTableClick: () -> Unit,
                      detail: Boolean) {
    val sortedUsers = users.sortedByDescending {
        it.points ?: 0
    }.take(if (detail) 50 else 5)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Title
        if(detail==false){
            Text(
                text = "Leaderboard",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Top 5 users
        sortedUsers.forEachIndexed { index, user ->
            Line(Color(0xAE222222))
            LeaderboardItem(
                rank = index + 1,
                username = user.username ?: "",
                points = user.points.toString() ?: "0",
                detail = detail
            )

        }
        Line(Color(0x7E414141))
        if(!detail){
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            ){
                transparentButton(text = "View table", onViewTableClick)
            }
        }

    }
}