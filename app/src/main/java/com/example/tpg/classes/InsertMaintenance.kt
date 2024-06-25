package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class InsertMaintenance(
    val id: Int,
    val description: String,
    val user_id: String,
    val machine_id: String,
    val image_link: String? = null
)
