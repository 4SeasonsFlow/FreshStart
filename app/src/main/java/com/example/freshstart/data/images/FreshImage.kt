package com.example.freshstart.data.images

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FreshImage(
    val id: String,
    @SerialName(value = "slug")
    val title: String,
    val urls: Map<String, String>,
    @SerialName(value = "user")
    val artistInfo: FreshImageArtist,
)

@Serializable
data class FreshImageArtist(
    @SerialName(value = "username")
    val artistName: String = "",
    val links: Map<String,String>,
    @SerialName(value = "profile_image")
    val profileImage: Map<String,String>
)
