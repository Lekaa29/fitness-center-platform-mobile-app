package com.example.fitnesscentarchat.ui.screens.membership.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.MembershipPackage



@Composable
fun MembershipItem(
    membershipPackage: MembershipPackage,
    onClick: () -> Unit // Changed to simple callback
) {
    val title = membershipPackage.title ?: "title"
    val discount = membershipPackage.discount
    var price = String.format("%.2f", membershipPackage.price)

    if(discount != null && discount != 0){
        val fraction = discount ?: 0
        price = String.format("%.2f", membershipPackage.price*(1-(fraction/100.0f)))

    }
    Log.d("item", "$membershipPackage")

    Column(
        modifier = Modifier
            .height(180.dp)
            .width(230.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        println("DEBUG: Item clicked - $title")
                        onClick() // Call the onClick callback
                    }
                )
            }
            .background(
                color = Color(0x40504F4F),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        if (discount != null && discount != 0) {
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Spacer(modifier = Modifier.height(30.dp))
        }

        if (discount != null && discount != 0) {
            Text(
                text = "$discount% OFF",
                fontWeight = FontWeight.Normal,
                color = Color.Red,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0x001B1B1B), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${price} Eur",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun MembershipItems(
    membershipPackages: List<MembershipPackage>,
    onItemClick: (Int) -> Unit // Changed to pass index
) {
    Log.d("membership", "${membershipPackages.size}")
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(12.dp)
    ) {
        itemsIndexed(membershipPackages) { index, membershipPackage ->
            Log.d("package", "$membershipPackage")
            MembershipItem(
                membershipPackage = membershipPackage,
                onClick = { onItemClick(index) } // Pass the index when clicked
            )
        }
    }
}