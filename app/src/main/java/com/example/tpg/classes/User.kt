package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
)