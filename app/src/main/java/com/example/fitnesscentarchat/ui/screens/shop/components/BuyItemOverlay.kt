package com.example.fitnesscentarchat.ui.screens.shop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.ShopItem


@Composable
fun BuyItemOverlay(shopItem: ShopItem?, onBackClick: (Boolean) -> Unit) {
    val title = shopItem?.Name ?: "-"
    val price = shopItem?.Price?.toFloat() ?: 0.0f
    val loyaltyPrice = shopItem?.LoyaltyPrice?.toInt() ?: 0

    val url = shopItem?.PictureUrl ?: ""

    var quantity by remember { mutableStateOf(1) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.8f)
                .background(
                    Color(0xFF1A1A1A),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick(false) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Order",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Profile section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

                ) {
                AsyncImage(
                    model = url,
                    contentDescription = title,
                    modifier = Modifier
                        .background(color=Color(0x20616161), shape= RoundedCornerShape(8.dp))
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )

                // User info inputs
            }
            Spacer(modifier = Modifier.height(8.dp))


            Text("$title", color=Color.White, fontWeight=FontWeight.Bold, fontSize=24.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Quantity",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = quantity.toString(),
                onValueChange = {
                    quantity = it.toIntOrNull() ?: 0  // safer parsing
                },
                textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp),
                placeholder = {
                    Text(
                        text = "1",
                        color = Color(0xFF888888)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0xFF2A2A2A),
                    unfocusedContainerColor = Color(0xFF2A2A2A),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color(0xFF888888),
                    unfocusedPlaceholderColor = Color(0xFF888888)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .border(
                        1.dp,
                        Color(0xFF444444),
                        shape = RoundedCornerShape(8.dp)
                    ),
                singleLine = true
            )




            Spacer(modifier = Modifier.height(20.dp))


            // Save button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        // Handle save logic here
                        onBackClick(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp).width(130.dp)
                ) {
                    Text(
                        text = "Pay ${price*quantity}E",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }


                Button(
                    onClick = {
                        // Handle save logic here
                        onBackClick(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC45B3C)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp).width(140.dp)
                ) {
                    Text(
                        text = "Pay ${loyaltyPrice*quantity}pts",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}