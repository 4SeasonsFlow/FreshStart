package com.example.freshstart.data.images

import com.example.freshstart.BuildConfig.UNSPLASH_API_KEY
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

interface ImageApiService {
    @GET("photos/random?client_id=${UNSPLASH_API_KEY}")
    suspend fun getRandomImage(): FreshImage
}
