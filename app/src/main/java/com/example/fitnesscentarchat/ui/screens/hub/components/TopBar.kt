package com.example.fitnesscentarchat.ui.screens.hub.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.map.components.TopBarMapButton
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import coil.request.ImageRequest


@Composable
private fun CustomMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val contentColor = if (isDestructive) {
        Color(0xFFDC3545)
    } else {
        Color.Black
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isDestructive) Color(0xFFFFF5F5) else Color.Transparent,
        animationSpec = tween(200),
        label = "background_color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BackgroundTopBar(
    searchText: String,
    isSearchExpanded: Boolean,
    onSearchExpandedChange: (Boolean) -> Unit,
    onSearchTextChange: (String) -> Unit,
    currentUser:User?,
    onMapClick: () -> Unit
) {



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer(alpha = 1.0f)
            .zIndex(1f)
    ) {

        TopBarContent(
            isSearchExpanded = isSearchExpanded,
            searchText = searchText,
            onSearchTextChange = { onSearchTextChange(it) },
            onSearchExpandChange = { onSearchExpandedChange(true)  },
            onMapClick = onMapClick,
            currentUser = currentUser
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(
    isSearchExpanded: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchExpandChange: (Boolean) -> Unit,
    onMapClick: () -> Unit,
    currentUser: User?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp, top = 8.dp)
    ) {
        // Left-aligned title
        Box(
            modifier = Modifier
                .background(
                    Color(0x00000000),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            if(isSearchExpanded == false){
                Text(
                    text = "FitnessCentar",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
            }
        }

        // Right-aligned row of icons/search bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            SearchBar(
                isExpanded = isSearchExpanded,
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onExpandChange = onSearchExpandChange
            )

            Spacer(modifier = Modifier.size(16.dp))
            TopBarMapButton(onMapClick = onMapClick)
            Spacer(modifier = Modifier.size(16.dp))
            ProfileButtonWithDropdown(
                currentUser = currentUser,
                size = 36.dp,
                onMessagesClick = { /* Handle messages */ },
                onItemsClick = { /* Handle items */ },
                onLogoutClick = { /* Handle logout */ }
            )
        }
    }
}


@Composable
fun ProfileButtonWithDropdown(
    currentUser: User?,
    size: Dp,
    onMessagesClick: () -> Unit = {},
    onItemsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        // Profile button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF0000))
                .clickable { isExpanded = !isExpanded },
            contentAlignment = Alignment.Center
        ) {
            if (currentUser?.pictureLink != null && currentUser.pictureLink != "") {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUser.pictureLink)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )
            } else {
                Text(
                    text = "${currentUser?.firstName?.first()}${currentUser?.lastName?.first()}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Dropdown menu with custom styling
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(300)) +
                    scaleIn(
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        initialScale = 0.8f,
                        transformOrigin = TransformOrigin(1f, 0f)
                    ),
            exit = fadeOut(animationSpec = tween(200)) +
                    scaleOut(
                        animationSpec = tween(200, easing = FastOutLinearInEasing),
                        targetScale = 0.8f,
                        transformOrigin = TransformOrigin(1f, 0f)
                    )
        ) {
            Surface(
                modifier = Modifier
                    .width(160.dp)
                    .wrapContentHeight()
                    .offset(x = (-120).dp, y = 48.dp)
                    .zIndex(10f),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 8.dp,
                color = Color.White,
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.08f))
            ) {
                Column {
                    CustomMenuItem(
                        icon = Icons.Default.Message,
                        text = "Messages",
                        onClick = {
                            onMessagesClick()
                            isExpanded = false
                        }
                    )

                    CustomMenuItem(
                        icon = Icons.Default.Inventory2,
                        text = "Items",
                        onClick = {
                            onItemsClick()
                            isExpanded = false
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.Black.copy(alpha = 0.1f)
                    )

                    CustomMenuItem(
                        icon = Icons.Default.ExitToApp,
                        text = "Log Out",
                        onClick = {
                            onLogoutClick()
                            isExpanded = false
                        },
                        isDestructive = true
                    )
                }
            }
        }
    }

    // Handle outside clicks by detecting when the dropdown loses focus
    // This is a cleaner approach that won't interfere with layout
    if (isExpanded) {
        // Auto-dismiss after a delay or on back press
        BackHandler(enabled = isExpanded) {
            isExpanded = false
        }
    }
}