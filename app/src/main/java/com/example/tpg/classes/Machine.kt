package com.example.tpg.classes

import kotlinx.serialization.Serializable

@Serializable
data class Machine(
    val serial_number: String,
    val created_at: String,
    val type: String,
    val model: String,
    val brand: String,
    val service_start_date: String,
    val hours_used: Int,
    val interventions_count: Int,
    val origin: String,
    val photo_link: String,
)
