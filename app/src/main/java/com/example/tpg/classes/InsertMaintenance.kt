package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class InsertMaintenance(
    val id: Int,
    val description: String,
    val user_id: Int,
    val machine_id: String,
)
