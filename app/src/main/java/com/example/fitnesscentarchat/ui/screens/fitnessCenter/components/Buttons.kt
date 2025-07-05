package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.fitnesscentarchat.R
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.hub.components.ProfileButtonWithDropdown


@Composable
fun transparentButton(text: String, onClick: () -> Unit){
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .height(44.dp) ,// smaller height
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.1f), // translucent background
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp), // compact padding
        shape = RoundedCornerShape(18.dp) // optional: small round corners
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.9f), // slightly transparent text
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun TopActionsContainer(soonLeaving: Int?, onViewQrClick: () -> Unit) {
    val newSoonLeaving = soonLeaving ?: 0

    Box (
        modifier = Modifier.height(114.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(0.7f), // Ensures the row spans the width
            horizontalArrangement = Arrangement.SpaceBetween, // Even spacing
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                Row(){
                    Text(text = "$newSoonLeaving", fontSize = 72.sp, color = Color(0xFFFFFFFF))

                    Image(
                        painter = painterResource(id = R.drawable.icons8_exit_100),
                        contentDescription = "Star Icon",
                        modifier = Modifier.padding(start=4.dp)
                    )
                }
                Text(text = "soon leaving", fontSize = 12.sp, color = Color(0xF2BDBDBD))
            }
            0xF2BDBDBD
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.icons8_qr_100),
                        contentDescription = "Star Icon",
                        modifier = Modifier
                            .size(84.dp)
                            .clickable {
                                onViewQrClick()
                            }
                    )
                }
                Text(text = "scan QR", fontSize = 12.sp, color = Color(0xF2BDBDBD))
            }
        }
    }

}


@Composable
fun rememberAnimatedGradientColor(color: String): Color {
    val infiniteTransition = rememberInfiniteTransition()

    val cleanHex = color.removePrefix("0x")
    val newColor = Color(android.graphics.Color.parseColor("#$cleanHex"))
    val targetColor = newColor.copy(
        red = (newColor.red * 0.7f).coerceIn(0f, 1f),
        green = (newColor.green * 0.7f).coerceIn(0f, 1f),
        blue = (newColor.blue * 0.7f).coerceIn(0f, 1f)
    )

    return infiniteTransition.animateColor(
        initialValue = newColor,
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    ).value
}

@Composable
fun Line(color: Color){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color)
        .padding(top = 8.dp, bottom = 8.dp))
    {

    }
}

@Composable
fun TopBarContent(currentUser: User?
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp, top = 2.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileButtonWithDropdown(
            currentUser = currentUser,
            size = 40.dp,
            onMessagesClick = { /* Handle messages */ },
            onItemsClick = { /* Handle items */ },
            onLogoutClick = { /* Handle logout */ }
        )
    }
}


@Composable
fun BackgroundTopBar(
    showNewsOverlay: Boolean,
    currentUser: User?
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer(alpha = 1.0f)
            .zIndex(1f)
    ) {
        if(showNewsOverlay==false){
            TopBarContent(
                currentUser
            )

        }

    }
}
