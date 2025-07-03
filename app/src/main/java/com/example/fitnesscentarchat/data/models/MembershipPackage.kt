package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class MembershipPackage(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "price") val price: Float,
    @Json(name = "days") val days: Int,
    @Json(name = "discount") val discount: Int?,
    @Json(name = "fitnessCentarName") val fitnessCentarName: String?,
    @Json(name = "idFitnessCentar") val idFitnessCentar: Int,
)

