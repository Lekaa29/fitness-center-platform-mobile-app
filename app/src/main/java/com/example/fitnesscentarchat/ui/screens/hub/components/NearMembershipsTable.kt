package com.example.fitnesscentarchat.ui.screens.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.FitnessCenter

@Composable
fun NearMembershipsTable(
    onFitnessCenterSelected: (Int) -> Unit,
    onGymLocationClick: (FitnessCenter) -> Unit = {},
    fitnessCenters: List<FitnessCenter>// Add callback parameter
) {

    Spacer(modifier = Modifier.height(20.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0x48000000))
    ) {
        // Header
        Row {
            Text(
                text = "Gyms near you",
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                color = Color(0xFFC0C0C0),
                modifier = Modifier.padding(start = 30.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // Gym list with LazyColumn
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(fitnessCenters) { index, center ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.width(140.dp)
                    ) {

                        Text(
                            text = center.name ?: "",
                            color = Color(0xFFFFFFFF),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                onFitnessCenterSelected(center.idFitnessCentar) // Call the callback with selected gym
                            }
                        )
                    }

                    Text(
                        text = distanceToString(center.distanceInMeters),
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "View Location",
                        color = Color(0xFF6DE3F3),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onGymLocationClick(center) // Call the callback with selected gym
                        }
                    )
                }

                // Divider (except for last item)
                if (index < fitnessCenters.size - 1) {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth(0.8f)
                            .background(color = Color(0xFF1A1A1A))
                    )
                }
            }

        }
    }
}

fun distanceToString(distance: Double?): String {
    val newDistance = distance ?: 0.0
    return if (newDistance > 1000) {
        String.format("%.1f km", newDistance / 1000)
    } else {
        "${Math.round(newDistance)} m"
    }
}
