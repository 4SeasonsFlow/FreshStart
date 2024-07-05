package com.example.freshstart.data.quotes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FreshQuote (
    @SerialName(value = "q")
    val quote: String,
    @SerialName(value = "a")
    val quoteAuthor: String
)