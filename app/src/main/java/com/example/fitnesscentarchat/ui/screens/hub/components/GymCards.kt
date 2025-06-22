package com.example.fitnesscentarchat.ui.screens.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun gymCard(
    onFitnessCenterSelected: (Int) -> Unit,
    gymName: String?,
    description: String?,
    imageUrl: String?,
    itemWidth: Dp,
    itemHeight: Dp,
    idFitnessCentar: Int
) {
    Box(
        modifier = Modifier
            .clickable {
                onFitnessCenterSelected(idFitnessCentar)}
            .width(itemWidth)
            .height(itemHeight)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
    ) {
        // Background Image
        AsyncImage(
            model = imageUrl,
            contentDescription = gymName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 50f // adjust this as needed for effect
                    )
                )
        )

        // Text content
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(start = 32.dp, bottom = 32.dp)
        ) {
            Text(
                text = gymName ?: "",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = description ?: "",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}



@Composable
fun gymPromoCard(
    onFitnessCenterSelected: (Int) -> Unit,
    gymName: String,
    description: String,
    imageUrl: String?,
    itemWidth: Dp,
    itemHeight: Dp,
    idFitnessCentar: Int
) {
    val grayScaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

    Box(
        modifier = Modifier
            .clickable {
            onFitnessCenterSelected(idFitnessCentar) // Call the callback with selected gym
        }
            .width(itemWidth)
            .height(itemHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray)
    ) {
        AsyncImage(
            model = "https://pngimg.com/uploads/percent/percent_PNG23.png",
            contentDescription = gymName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .background(Color(0xFFD41D16)),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x00CCCCCC), Color(0xFF777777)),
                    )
                )
        )

        // Text content
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(18.dp)
                .fillMaxWidth()

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = gymName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = description,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
