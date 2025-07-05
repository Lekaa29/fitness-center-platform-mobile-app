package com.example.fitnesscentarchat.ui.screens.profile.components

import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.R
import com.example.fitnesscentarchat.data.models.User


@Composable
fun TopTextSection(user: User?, onTopTextPositioned: (Float) -> Unit, onEditProfileChange: () -> Unit) {
    var creationDate = formatDateString(user?.creationDate ?: "")
    creationDate = "Joined ${creationDate}"
    Row(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth()
            .onGloballyPositioned {
                onTopTextPositioned(it.positionInParent().y)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start=50.dp)
        ) {
            AsyncImage(
                model = user?.pictureLink ?: "",
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp) // Set a specific size instead of fillMaxSize
                    .clip(CircleShape) // Makes the image circular
                    .background(Color(0xFFFFFF00))
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp)

                .padding(start = 30.dp)

        ) {
            // Add username at the top

            Text(
                text = user?.username ?: "",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,

                )
            Text(
                text = creationDate ?: "joined ?",
                color = Color(0xFFB9B9B9),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top=20.dp)
            )


        }

        Box(
            modifier = Modifier
                .size(52.dp)
                .padding(bottom = 20.dp, end = 10.dp)
                .clickable {
                    onEditProfileChange()
                    Log.d("clicked", "clicked")
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icons8_edit_100),
                contentDescription = "Edit Icon",
                modifier = Modifier.size(32.dp) // Smaller than the clickable area
            )
        }
    }

}



fun formatDateString(input: String): String {
    if (input.isBlank()) return ""

    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val date = inputFormat.parse(input)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        Log.e("DateParsing", "Error parsing date: $input", e)
        "Invalid Date"
    }
}
