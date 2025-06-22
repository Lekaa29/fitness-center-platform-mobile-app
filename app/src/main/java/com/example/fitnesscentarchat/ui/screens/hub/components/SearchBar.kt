package com.example.fitnesscentarchat.ui.screens.hub.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import java.time.LocalDateTime
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedSearchField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit
) {
    androidx.compose.material3.TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = { Text("Search") },
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            Color.Transparent,
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Close Search",
                tint = Color.White,
                modifier = Modifier.clickable {
                    onExpandChange(false)
                }
            )
        }
    )
}

@Composable
fun CollapsedSearchButton(
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                Color(0x00808080),
                CircleShape
            )
            .clickable {
                onExpandChange(true)

                Log.d("CLICK", "CLICKED")
            },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    isExpanded: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val width by animateDpAsState(
        targetValue = if (isExpanded) 300.dp else 40.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .size(height = 40.dp, width = width)
            .background(
                color = Color(0xAA808080),
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(enabled = !isExpanded) { onExpandChange(true) },
        contentAlignment = Alignment.CenterStart
    ) {
        if (isExpanded) {
            ExpandedSearchField(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onExpandChange = onExpandChange
            )
        } else {
            CollapsedSearchButton(isExpanded, onExpandChange)
        }
    }
}
