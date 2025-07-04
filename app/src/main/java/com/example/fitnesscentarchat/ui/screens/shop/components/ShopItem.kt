package com.example.fitnesscentarchat.ui.screens.shop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.ShopItem

@Composable
fun ShopItem(shopItem: ShopItem, onItemClick: (ShopItem) -> Unit) {
    val title = shopItem.Name ?: "-"
    val price = String.format("%.2f", shopItem.Price)
    val loyaltyPrice = shopItem.LoyaltyPrice.toInt() ?: "-"
    val url = shopItem.PictureUrl ?: ""

    Column(
        modifier = Modifier
            .height(180.dp)
            .width(170.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        println("DEBUG: Item clicked - $title")
                        onItemClick(shopItem) // Pass the actual item instead of boolean
                    }
                )
            }
            .background(
                color = Color(0x27CECECE),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color(0xFFFFFFFF))
        )

        Spacer(modifier = Modifier.height(0.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 12.sp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1B1B1B), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text(text = "${price} Eur", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFFF67B6B), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "${loyaltyPrice} pts",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ShopItems(shopItems: List<ShopItem>, onItemSelected: (ShopItem) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .height(700.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(shopItems.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in rowItems) {
                    ShopItem(shopItem = item, onItemClick = onItemSelected)
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.width(170.dp))
                }
            }
        }
    }
}