package com.example.fitnesscentarchat.ui.screens.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnesscentarchat.data.models.MembershipModel


@Composable
fun UserMembershipsRow(
    onFitnessCenterSelected: (Int) -> Unit,
    usersMemberships: List<MembershipModel>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val itemWidth = screenWidth * 0.85f
    val itemHeight = screenHeight * 0.3f


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight + 20.dp) // ensures same height as background
    ) {
        // Blurred background
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0x0AFFFFFF))
                .blur(40.dp)
        )

        Text(
            text = "Your memberships",
            color = Color(0xFFA2A2A2),
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(start = 16.dp, top=10.dp, bottom=10.dp)
        )

        // Centered LazyRow vertically
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 20.dp), // ← Center the LazyRow in the parent Box
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(usersMemberships) { membership ->
                gymCard(onFitnessCenterSelected, membership.fitnessCentarName, membership.membershipDeadline, membership.fitnessCentarBannerUrl, itemWidth, itemHeight-36.dp,
                membership.idFitnessCentar)
            }
        }
    }
}