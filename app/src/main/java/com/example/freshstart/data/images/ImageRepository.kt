package com.example.freshstart.data.images

interface ImageRepository {
    suspend fun getRandomImage(): FreshImage
}

class NetworkImageRepository(
    private val imageApiService: ImageApiService
) : ImageRepository {
    override suspend fun getRandomImage(): FreshImage = imageApiService.getRandomImage()
}