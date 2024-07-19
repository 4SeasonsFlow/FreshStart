package com.example.freshstart.ui

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.freshstart.R
import com.example.freshstart.data.images.FreshImage
import com.example.freshstart.data.images.FreshImageArtist
import com.example.freshstart.data.mockImage
import com.example.freshstart.data.quotes.FreshQuote
import com.example.freshstart.ui.theme.FreshStartTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (viewModel.uiState) {
        is HomeUiState.Loading -> LoadingScreen(modifier.fillMaxSize())
        is HomeUiState.Success -> FreshStartScreen(
            word = (viewModel.uiState as HomeUiState.Success).freshWord,
            image = (viewModel.uiState as HomeUiState.Success).freshImage,
            quote = (viewModel.uiState as HomeUiState.Success).freshQuote,
            modifier = modifier,
            contentPadding = contentPadding,
            onRefresh = viewModel::getFreshCard,
            isRefreshing = viewModel.isRefreshing,
            onImageClick = viewModel::showBottomSheet,
            hideBottomSheet = viewModel::hideBottomSheet,
            isBottomSheetVisible = viewModel.isBottomSheetVisible
        )

        is HomeUiState.Error -> ErrorScreen(
            retryAction = viewModel::getFreshCard,//getRandomImage,
            modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreshStartScreen(
    word: Int,
    image: FreshImage,
    quote: List<FreshQuote>,
    onRefresh: () -> Unit,
    onImageClick: () -> Unit,
    hideBottomSheet: () -> Unit,
    isBottomSheetVisible: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isRefreshing: Boolean
) {
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet1 by remember { mutableStateOf(false) }

    if (showBottomSheet1) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet1 = false
            },
            sheetState = bottomSheetState,
            modifier = Modifier
        ) {
            // Sheet content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageDetailsScreen(
                    image = image,
                    artist = image.artistInfo,
                    onImageClick = { showBottomSheet1 = true },
                    modifier = Modifier
                )
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        PullToRefreshColumn(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            contentPadding = contentPadding,
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FreshStartCard(
                word = word,
                image = image,
                quote = quote,
                onImageClick = { showBottomSheet1 = true },
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.loading))
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = null,

            )
        Text(text = stringResource(R.string.error))
        Button(onClick = retryAction) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun FreshStartCard(
    word: Int,
    image: FreshImage,//,
    quote: List<FreshQuote>,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        modifier = modifier
            //.fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
        ) {
            //Fresh Image
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                //.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ImageBox(
                    image = image,
                    //contentDescription = null,
                    modifier = Modifier,
                    onImageClick = onImageClick,
                )
            }
            //Fresh Quote
            QuoteBox(
                quoteText = quote[0].quote,
                quoteAuthor = quote[0].quoteAuthor,
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageBox(
    image: FreshImage,
    //@StringRes contentDescription: Int,
    modifier: Modifier = Modifier,
    imageType: String = "regular",
    onImageClick: () -> Unit,
    isClickable: Boolean = true
) {
    Box(modifier = modifier
        .combinedClickable(
            enabled = isClickable,
            onLongClick = {
                onImageClick()
            }
            ) {}

    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.urls[imageType])
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteBox(
    quoteText: String,
    quoteAuthor: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val quoteAttribution = stringResource(id = R.string.zenqoutes_attribution)
    val quoteSourceUrl = stringResource(id = R.string.zenqoutes_url)
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                action = {
                    TextButton(onClick = { scope.launch {
                        followLink(quoteSourceUrl, context)
                        tooltipState.dismiss()
                    } }) {
                        Text(text = "Source")
                    }
                }
            ) { Text(text = quoteAttribution) }
        },
        state = tooltipState
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quoteText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.fillMaxWidth(0.25f))
                Text(
                    text = "- $quoteAuthor",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontSize = 20.sp
                )
            }
        }

    }
}

@Composable
fun ImageDetailsScreen(
    image: FreshImage,
    artist: FreshImageArtist,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val artistAttribution = artist.links["html"] + stringResource(id = R.string.utm_parameters)
        val unsplashAttribution =
            "https://unsplash.com/" + stringResource(id = R.string.utm_parameters)

        val annotatedString = buildAnnotatedString {
            append("Photo by ")
            pushStringAnnotation(tag = "URL", annotation = artistAttribution)
            withStyle(
                style = SpanStyle(
                    color = Color.Blue, textDecoration = TextDecoration.Underline
                )
            ) {
                append(artist.artistName)
            }
            pop()
            append(" on ")
            pushStringAnnotation(tag = "URL", annotation = unsplashAttribution)
            withStyle(
                style = SpanStyle(
                    color = Color.Blue, textDecoration = TextDecoration.Underline
                )
            ) {
                append("Unsplash")
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            style = TextStyle(
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        ) { offset ->
            // We check if there is an *URL* annotation attached to the text
            // at the clicked position
            annotatedString.getStringAnnotations(
                tag = "URL", start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                // If yes, we log its value
                Log.d("Clicked URL", annotation.item)
                followLink(annotation.item, context)
            }
        }

        ImageBox(
            image = image,
            imageType = "full",
            isClickable = false,
            onImageClick = onImageClick
        )
    }
}

fun followLink(url: String, context: Context) {
    val webIntent: Intent = Uri.parse(url).let { webpage ->
        Intent(Intent.ACTION_VIEW, webpage)
    }
    try {
        context.startActivity(webIntent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

/*@Preview
@Composable
fun ImageDetailsScreenPreview() {
    FreshStartTheme {
        ImageDetailsScreen(
            image = mockImage,
            artist = mockArtist
        )
    }
}*/

@Preview
@Composable
fun FreshStartCardPreview() {
    FreshStartTheme {
        FreshStartCard(
            word = R.string.placeholder_word,
            image = mockImage,
            quote = listOf(
                FreshQuote(
                    quote = stringResource(id = R.string.placeholder_quote),
                    quoteAuthor = stringResource(id = R.string.placeholder_author),
                ),
            ),
            onImageClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    FreshStartTheme {
        FreshStartCard(
            word = R.string.placeholder_word,
            image = mockImage,
            quote = listOf(
                FreshQuote(
                    quote = stringResource(id = R.string.placeholder_quote),
                    quoteAuthor = stringResource(id = R.string.placeholder_author)
                )
            ),
            onImageClick = {}
        )
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    FreshStartTheme {
        ErrorScreen(
            retryAction = { /*TODO*/ }
        )
    }
}
