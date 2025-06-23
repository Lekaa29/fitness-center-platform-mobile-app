package com.example.fitnesscentarchat.ui.screens.membership.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.User

@Composable
fun TopTextSection(user: User, membership: MembershipModel?, onTopTextPositioned: (Float) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Box(
                    modifier = Modifier.border(
                        1.dp,
                        Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(8.dp)
                    )
                        .background(color= Color(0x6B191919), shape = RoundedCornerShape(8.dp)
                        ),
                ){
                    Text("MEMBERSHIP", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text("$user.username", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp), fontSize=20.sp)
                Text("Expires: ${membership?.membershipDeadline ?: ""}", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp), fontSize=16.sp)
            }
        }
        Spacer(modifier = Modifier.size(30.dp))


    }


}