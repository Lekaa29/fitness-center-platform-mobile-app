package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class MembershipPackage(
    @Json(name = "id") val Id: Int,
    @Json(name = "title") val Title: String,
    @Json(name = "price") val Price: Float,
    @Json(name = "days") val Days: Int?,
    @Json(name = "discount") val Discount: Int?,
    @Json(name = "idFitnessCentar") val IdFitnessCentar: Int,
)

