package com.example.fitnesscentarchat.ui.screens.shop.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.MembershipModel


@Composable
fun TopTextSection(fitnessCenterName:String?, userMembership: MembershipModel?) {
    val pointsBalance = userMembership?.loyaltyPoints ?: 0
    val title = fitnessCenterName ?: ""
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0x6B191919), shape = RoundedCornerShape(8.dp)
                        ),
                ){
                    Text("${title}", color=Color(0xFFFFFFFF),modifier=Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0x6B191919), shape = RoundedCornerShape(8.dp)
                        ),
                ){
                    Text("SHOP", color=Color(0xFFFFFFFF),modifier=Modifier.padding(8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        GradientBorderBox(pointsBalance.toString())
        Spacer(modifier = Modifier.size(10.dp))

    }


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
fun GradientBorderBox(points: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(110.dp, 48.dp)
            .border(
                BorderStroke(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Magenta, Color.Cyan)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp)) // Optional background
            .padding(4.dp)
    ) {
        Text(
            text = "$points PTS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}