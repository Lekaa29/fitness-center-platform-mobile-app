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
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest


@Composable
private fun CustomMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val contentColor = if (isDestructive) {
        Color(0xFFFF6B6B) // Lighter red for dark theme
    } else {
        Color(0xFFE1E1E1) // Light gray for dark theme
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isDestructive) Color(0xFF2D1B1B) else Color.Transparent, // Dark red background
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
    onMapClick: () -> Unit,
    onUserChatSelect: () -> Unit,
    onUserItemsSelect: () -> Unit


) {



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Color(0x001A1A1A)) // Dark background
            .graphicsLayer(alpha = 1.0f)
            .zIndex(1f)
    ) {

        TopBarContent(
            isSearchExpanded = isSearchExpanded,
            searchText = searchText,
            onSearchTextChange = { onSearchTextChange(it) },
            onSearchExpandChange = { onSearchExpandedChange(true)  },
            onMapClick = onMapClick,
            currentUser = currentUser,
            onUserChatSelect=onUserChatSelect,
            onUserItemsSelect=onUserItemsSelect
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
    currentUser: User?,
    onUserChatSelect: () -> Unit,
    onUserItemsSelect: () -> Unit


) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0x001A1A1A)) // Dark background
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
                    color = Color(0xFFE1E1E1), // Light gray for dark theme
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
                onMessagesClick = { onUserChatSelect() },
                onItemsClick = { onUserItemsSelect() },
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
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF2D2D2D)) // Dark gray background
                .clickable { isExpanded = !isExpanded },
            contentAlignment = Alignment.Center
        ) {
            if (currentUser?.pictureLink?.isNotEmpty() == true) {
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
                    text = "${currentUser?.firstName?.firstOrNull() ?: ""}${currentUser?.lastName?.firstOrNull() ?: ""}",
                    color = Color(0xFFE1E1E1), // Light gray text
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(160.dp)
                .background(Color(0xFF000000)) // Dark dropdown background
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Messages",
                        color = Color(0xFFE1E1E1) // Light gray text
                    )
                },
                onClick = {
                    onMessagesClick()
                    isExpanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Message,
                        contentDescription = null,
                        tint = Color(0xFFE1E1E1) // Light gray icon
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "Items",
                        color = Color(0xFFE1E1E1) // Light gray text
                    )
                },
                onClick = {
                    onItemsClick()
                    isExpanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = Color(0xFFE1E1E1) // Light gray icon
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "Log Out",
                        color = Color(0xFFFF6B6B) // Light red for dark theme
                    )
                },
                onClick = {
                    onLogoutClick()
                    isExpanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B) // Light red icon
                    )
                }
            )
        }
    }
}