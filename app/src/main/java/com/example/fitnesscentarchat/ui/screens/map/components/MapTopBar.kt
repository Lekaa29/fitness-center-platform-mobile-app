package com.example.fitnesscentarchat.ui.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.hub.components.ProfileButtonWithDropdown

@Composable
fun TopBarMapButton(onMapClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                Color(0xA3808080),
                CircleShape
            )
            .clickable { onMapClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun MapButton(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                Color(0x99291CE6),
                CircleShape
            )
            .clickable { onBackClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

    }
}


@Composable
fun MapTopBar(onBackClick: () -> Unit, modifier:Modifier, currentUser: User?) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .padding(end = 8.dp, top = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MapSearchButton()
            Spacer(modifier = Modifier.size(1.dp))
            MapButton(onBackClick = onBackClick)
            Spacer(modifier = Modifier.size(1.dp))
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
fun MapSearchButton() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                Color(0xAA808080),
                CircleShape
            )
            .clickable { /* Search */ },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}