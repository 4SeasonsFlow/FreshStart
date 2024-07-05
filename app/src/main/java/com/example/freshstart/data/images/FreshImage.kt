package com.example.freshstart.data.images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FreshImage(
    val id: String,
    @SerialName(value = "slug")
    val title: String,
    val urls: Map<String, String>,
)
