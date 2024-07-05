package com.example.freshstart.data.quotes

import retrofit2.http.GET

interface QuoteApiService {
    @GET("api/random")
    suspend fun getRandomQuote(): List<FreshQuote>
}
