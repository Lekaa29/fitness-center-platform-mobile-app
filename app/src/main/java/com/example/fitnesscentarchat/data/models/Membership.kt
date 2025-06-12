package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class Membership(
    @Json(name = "idMembership") val IdMembership: Int,
    @Json(name = "idUser") val IdUser: Int,
    @Json(name = "idFitnessCentar") val IdFitnessCentar: Int,
    @Json(name = "loyaltyPoints") val LoyaltyPoints: Int?,
    @Json(name = "streakRunCount") val StreakRunCount: Int?,
    @Json(name = "membershipDeadline") val MembershipDeadline: String?
)