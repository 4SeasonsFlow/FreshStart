package com.example.freshstart.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.freshstart.FreshStartApplication
import com.example.freshstart.R
import com.example.freshstart.data.images.FreshImage
import com.example.freshstart.data.images.FreshImageArtist
import com.example.freshstart.data.images.ImageRepository
import com.example.freshstart.data.quotes.FreshQuote
import com.example.freshstart.data.quotes.QuoteRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface HomeUiState {
    data class Success(
        @StringRes var freshWord: Int = R.string.placeholder_word,
        var freshImage: FreshImage,
        var freshQuote: List<FreshQuote>,
        var freshImageArtist: FreshImageArtist
    ) : HomeUiState

    data object Error : HomeUiState
    data object Loading : HomeUiState
}

class HomeScreenViewModel(
    private val imageRepository: ImageRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {
    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var isBottomSheetVisible by mutableStateOf(false)

    init {
        getFreshCard()
    }

    private suspend fun getRandomImage(): FreshImage {
        val image: Deferred<FreshImage> =
            viewModelScope.async {
                imageRepository.getRandomImage()
            }
        return image.await()
    }

    private suspend fun getRandomQuote(): List<FreshQuote> {
        val quote: Deferred<List<FreshQuote>> =
            viewModelScope.async {
                quoteRepository.getRandomQuote()
            }
        return quote.await()
    }

    private fun getRandomImageArtist(image: FreshImage): FreshImageArtist {
        val artistInfo: FreshImageArtist = image.artistInfo
        return artistInfo
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            isBottomSheetVisible = true
        }
    }

    fun hideBottomSheet() {
        viewModelScope.launch {
            isBottomSheetVisible = false
        }
    }

    fun getFreshCard() {
        viewModelScope.launch {
            isRefreshing = true
            uiState = try {
                val newImage = getRandomImage()
                HomeUiState.Success(
                    freshImage = newImage,
                    freshQuote = getRandomQuote(),
                    freshImageArtist = getRandomImageArtist(newImage)
                )
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            } finally {
                isRefreshing = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FreshStartApplication)
                val imageRepository = application.container.imageRepository
                val quoteRepository = application.container.quoteRepository
                HomeScreenViewModel(
                    imageRepository = imageRepository,
                    quoteRepository = quoteRepository
                )
            }
        }
    }
}