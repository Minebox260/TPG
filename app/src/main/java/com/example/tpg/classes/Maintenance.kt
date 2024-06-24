package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class Maintenance(
    val id: Int,
    val created_at: String,
    val description: String,
    val image_link: String,
    val machines: Machine,
    val user_id: String
)