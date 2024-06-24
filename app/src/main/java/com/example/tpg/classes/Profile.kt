package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class Profile (
    val email: String,
    val user_id: String,
    val entreprise: String,
    val name: String,
    val phone: String,
    val profile_icon_url: String
)