package com.example.fitnesscentarchat.ui.screens.hub.components

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
import coil.request.ImageRequest
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.map.components.TopBarMapButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(
    isSearchExpanded: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchExpandChange: (Boolean) -> Unit,
    onMapClick: () -> Unit,
    currentUser: User
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
            ProfileButton(currentUser=currentUser, 36.dp)
        }
    }
}

@Composable
fun BackgroundTopBar(
    searchText: String,
    isSearchExpanded: Boolean,
    onSearchExpandedChange: (Boolean) -> Unit,
    onSearchTextChange: (String) -> Unit,
    currentUser:User,
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


@Composable
fun ProfileButton(currentUser: User, size: Dp) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFFF0000))
            .clickable { /* TODO: Profile click action */ },
        contentAlignment = Alignment.Center
    ) {
        if (currentUser.pictureLink != null) {
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
                text = "${currentUser.firstName.first()}${currentUser.lastName.first()}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


