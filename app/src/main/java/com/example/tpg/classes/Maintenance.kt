package com.example.tpg.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Maintenance(
    val id: Int,
    val created_at: String,
    val description: String,
    val image_link: String?,
    @SerialName("machines") val machine: Machine,
    @SerialName("profiles") val profile: Profile
)