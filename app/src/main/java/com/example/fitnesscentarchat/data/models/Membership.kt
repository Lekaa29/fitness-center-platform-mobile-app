package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class MembershipModel(
    @Json(name = "idMembership") val idMembership: Int,
    @Json(name = "idUser") val idUser: Int,
    @Json(name = "idMembershipPackage") val idMembershipPackage: Int?,
    @Json(name = "idFitnessCentar") val idFitnessCentar: Int,
    @Json(name = "points") val points: Int?,
    @Json(name = "loyaltyPoints") val loyaltyPoints: Int?,
    @Json(name = "streakRunCount") val streakRunCount: Int?,
    @Json(name = "fitnessCentarName") val fitnessCentarName: String?,
    @Json(name = "fitnessCentarBannerUrl") val fitnessCentarBannerUrl: String?,
    @Json(name = "fitnessCentarLogoUrl") val fitnessCentarLogoUrl: String?,
    @Json(name = "membershipDeadline") val membershipDeadline: String?,
    @Json(name = "username") val username: String?

)

