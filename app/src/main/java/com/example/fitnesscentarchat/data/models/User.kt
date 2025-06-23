package com.example.fitnesscentarchat.data.models
import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val id: Int,
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "username") val username: String,
    @Json(name = "creationDate") val creationDate: String? = null,
    @Json(name = "joinedDate") val joinedDate: String? = null,
    @Json(name = "pictureLink") val pictureLink: String? = null,


)