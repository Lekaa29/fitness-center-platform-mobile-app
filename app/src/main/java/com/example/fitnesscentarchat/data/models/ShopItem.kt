package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.time.LocalDateTime

data class ShopItem(
    @Json(name = "idShopItem") val IdShopItem: Int,
    @Json(name = "idFitnessCentar") val IdFitnessCentar: Int,
    @Json(name = "name") val Name: String?,
    @Json(name = "pictureUrl") val PictureUrl: String?,
    @Json(name = "price") val Price: BigDecimal,
    @Json(name = "loyaltyPrice") val LoyaltyPrice: BigDecimal?
)