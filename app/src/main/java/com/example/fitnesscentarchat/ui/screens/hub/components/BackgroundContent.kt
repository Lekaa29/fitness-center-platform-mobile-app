package com.example.fitnesscentarchat.ui.screens.hub.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.ui.screens.hub.HubUiState

@Composable
fun HubContent(
    onFitnessCenterSelected: (Int) -> Unit,
    uiState: HubUiState,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSearchExpanded: Boolean,
    searchText: String,
    onSearchExpandedChange: (Boolean) -> Unit,
    onSearchTextChange: (String) -> Unit,
    recentSearches: List<String>,
    onGymLocationClick: (FitnessCenter) -> Unit = {}) {
    val scrollState = rememberScrollState()

    var topTextOffsetY by remember { mutableStateOf(0f) }


    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundTopBar(
            searchText = searchText,
            onMapClick = onMapClick,
            isSearchExpanded = isSearchExpanded,
            onSearchExpandedChange = onSearchExpandedChange,
            onSearchTextChange = onSearchTextChange,
            currentUser = uiState.currentUser
        )
        Box(){
            BackgroundScrollableContent(
                onFitnessCenterSelected,
                scrollState = scrollState,
                onTopTextPositioned = { topTextOffsetY = it },
                onGymLocationClick,
                uiState=uiState
            )
        }
        Box(){
            if (isSearchExpanded) {
                // Fullscreen semi-transparent gray overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xF2111111))
                        .clickable { onSearchExpandedChange(false) }
                )

                // Column with SearchBar and recent searches
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xD90F0F0F))
                        .padding(16.dp)
                ) {


                    Spacer(modifier = Modifier
                        .height(12.dp)
                        .padding(top = 24.dp))

                    Text(
                        "Recent searches",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
                    )

                    androidx.compose.foundation.lazy.LazyColumn {
                        items(recentSearches.size) { index ->
                            Text(
                                recentSearches[index],
                                color = Color.LightGray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSearchTextChange(recentSearches[index])
                                        // Maybe do search here
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun BackgroundScrollableContent(
    onFitnessCenterSelected: (Int) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState,
    onTopTextPositioned: (Float) -> Unit,
    onGymLocationClick: (FitnessCenter) -> Unit = {},
    uiState: HubUiState
) {
    val firstAnimatedColor = rememberAnimatedGradientColor(Color(0xFF0E033F), Color(0xFF021674), 4000)
    val secondAnimatedColor = rememberAnimatedGradientColor(Color(0xFF0B0218), Color(0xFF0B0216), 5000)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        firstAnimatedColor,
                        secondAnimatedColor,
                        secondAnimatedColor

                    )
                )
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        UserMembershipsRow(onFitnessCenterSelected, uiState.usersMemberships)
        Spacer(modifier = Modifier.height(10.dp))
        PromoRow()
        PromotionMembershipsRow(uiState.promoFitnessCentars, onFitnessCenterSelected)

        NearMembershipsTable(onFitnessCenterSelected, onGymLocationClick, uiState.nearFitnessCentars)

    }
}


@Composable
fun PromoRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .height(40.dp)

    ) {
        // Blurred rounded background
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp)) // 👈 Rounded corners
                .background(
                    color = Color(0x16CCCCCC)
                )
                .blur(30.dp)
        )

        // Text on top
        Text(
            text = "PROMOTIONS & DISCOUNTS",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.Center)

        )
    }
}

@Composable
fun rememberAnimatedGradientColor(initalValue: Color, targetValue:Color, milis: Int): Color {
    val infiniteTransition = rememberInfiniteTransition()

    return infiniteTransition.animateColor(
        initialValue = initalValue,//Color(0xFFFFB74A),
        targetValue = targetValue,//Color(0xFFF8E16C),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = milis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    ).value
}