package com.example.freshstart.data

import com.example.freshstart.data.images.ImageApiService
import com.example.freshstart.data.images.ImageRepository
import com.example.freshstart.data.images.NetworkImageRepository
import com.example.freshstart.data.quotes.NetworkQuoteRepository
import com.example.freshstart.data.quotes.QuoteApiService
import com.example.freshstart.data.quotes.QuoteRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val imageRepository: ImageRepository
    val quoteRepository: QuoteRepository
}

class DefaultAppContainer : AppContainer {
    private val imageBaseUrl = "https://api.unsplash.com"
    private val quoteBaseUrl = "https://zenquotes.io"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofitImages = Retrofit.Builder()
        //.addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json
            .asConverterFactory("application/json".toMediaType()))
        .baseUrl(imageBaseUrl)
        .build()

    private val retrofitQuotes = Retrofit.Builder()
        .addConverterFactory(json
            .asConverterFactory("application/json".toMediaType()))
        .baseUrl(quoteBaseUrl)
        .build()

    private val retrofitServiceI : ImageApiService by lazy {
        retrofitImages.create(ImageApiService::class.java)
    }
    private val retrofitServiceQ : QuoteApiService by lazy {
        retrofitQuotes.create(QuoteApiService::class.java)
    }

    override val imageRepository: ImageRepository by lazy {
        NetworkImageRepository(retrofitServiceI)
    }
    override val quoteRepository: QuoteRepository by lazy {
        NetworkQuoteRepository(retrofitServiceQ)
    }
}