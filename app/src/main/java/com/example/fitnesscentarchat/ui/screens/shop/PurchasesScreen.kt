package com.example.fitnesscentarchat.ui.screens.shop

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.ui.screens.hub.ShopViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreen(
    viewModel: ShopViewModel,
    onNavigateBack: () -> Unit
) {
    val userItems by viewModel.userItems.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("PurchasesScreen", "LaunchedEffect triggered, calling getUserItems()")
        viewModel.getUserItems()
    }

    // Add logging for state changes
    LaunchedEffect(userItems) {
        Log.d("PurchasesScreen", "UserItems updated: ${userItems.size} items")
        if (userItems.isNotEmpty()) {
            userItems.forEachIndexed { index, item ->
                Log.d("PurchasesScreen", "Item $index: ${item.Name} - ${item.Price}")
            }
        } else {
            Log.d("PurchasesScreen", "UserItems is empty - showing empty state")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Add a top bar or header
        TopAppBar(
            title = {
                Text(
                    text = "My Purchases",
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black
            ),
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        )

        // Show content or empty state
        if (userItems.isNotEmpty()) {
            UserShopItems(userItems)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ShoppingBag,
                        contentDescription = "No purchases",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No purchases yet",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Visit the shop to buy items!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            Log.d("PurchasesScreen", "Refresh button clicked")
                            viewModel.refreshUserItems()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE)
                        )
                    ) {
                        Text("Refresh", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun UserShopItem(shopItem: ShopItem) {
    val title = shopItem.Name ?: "No Name"
    val price = try {
        String.format("%.2f", shopItem.Price ?: 0.0)
    } catch (e: Exception) {
        "0.00"
    }
    val loyaltyPrice = try {
        shopItem.LoyaltyPrice?.toInt()?.toString() ?: "0"
    } catch (e: Exception) {
        "0"
    }
    val url = shopItem.PictureUrl ?: ""

    Log.d("UserShopItem", "Rendering item: $title, Price: $price, URL: $url")

    Column(
        modifier = Modifier
            .height(170.dp)
            .width(170.dp)
            .background(
                color = Color(0x27CECECE),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = url,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            onError = { error ->
                Log.e("UserShopItem", "Error loading image for $title: ${error.result.throwable}")
            }
        )

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 12.sp
        )

        Text(
            text = "$$price",
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun UserShopItems(shopItems: List<ShopItem>) {
    Log.d("UserShopItems", "Rendering ${shopItems.size} items")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    UserShopItem(shopItem = item)
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.width(170.dp))
                }
            }
        }
    }
}