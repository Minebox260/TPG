package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Int,
    val email: String,
)