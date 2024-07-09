package com.example.freshstart.data

import com.example.freshstart.data.images.FreshImage
import com.example.freshstart.data.images.FreshImageArtist

val mockArtist = FreshImageArtist(
    artistName = "",
    links = mapOf("" to ""),
    profileImage = mapOf("" to ""),
)

val mockImage = FreshImage(
    id = "",
    title = "",
    urls = mapOf("" to ""),
    artistInfo = mockArtist,
)
