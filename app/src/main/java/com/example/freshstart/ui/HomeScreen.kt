package com.example.freshstart.ui

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
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
            onQuoteClick = viewModel::showQuoteSnackbar,//{ viewModel.showQuoteSnackbar("snackbar!!") }
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
    onQuoteClick: (String) -> Unit,
    onImageClick: () -> Unit,
    hideBottomSheet: () -> Unit,
    isBottomSheetVisible: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isRefreshing: Boolean
) {
    val scope = rememberCoroutineScope()
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
                /*Button(onClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet1 = false
                        }
                    }
                }) {
                    Text(text = "Hide Bottom Sheet")
                }*/
                ImageDetailsScreen(
                    image = image,
                    artist = image.artistInfo,
                    onImageClick = { showBottomSheet1 = true },
                    modifier = Modifier
                )
            }
        }
    }
    /*LaunchedEffect(key1 = true) {
        if (isBottomSheetVisible) {
            scope.launch {
                bottomSheetState.show()
            }
        } else {
            scope.launch {
                bottomSheetState.hide()
            }
        }
    }*/


    /*Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        FreshStartCard(
            //uiState = uiState,
            word, image, quote, author,
            modifier = Modifier,
            contentPadding = contentPadding
        )

    }*/
    Box(
        modifier = modifier//.padding(contentPadding)
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
                onQuoteClick = onQuoteClick,
                onImageClick = { showBottomSheet1 = true },//onImageClick,
                modifier = Modifier,
                //contentPadding = contentPadding
            )
            /*if (bottomSheetState.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch {
                            hideBottomSheet()
                            bottomSheetState.hide()
                        }
                    },
                ) {
                    Text("Hide bottom sheet")
                }

            }*/
            /*Button(onClick = { showBottomSheet1 = true }) {
                Text(text = "show bottom sheet")
            }*/
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

// populated with the "fresh material" each refresh/day
@Composable
fun FreshStartCard(
    word: Int,
    image: FreshImage,//,
    quote: List<FreshQuote>,
    onQuoteClick: (String) -> Unit,
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
                onQuoteClick = onQuoteClick
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
    onQuoteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val quoteAttribution = stringResource(id = R.string.zenqoutes_attribution)
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                action = {
                    TextButton(onClick = { scope.launch {
                        onQuoteClick("To Browser!!")
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
        val artistAttribution = artist.links["html"] + stringResource(id = R.string.utm_parameters)
        val unsplashAttribution =
            "https://unsplash.com/" + stringResource(id = R.string.utm_parameters)

        val annotatedString = buildAnnotatedString {
            append("Photo by ")
            pushStringAnnotation(tag = "artist attribution", annotation = artistAttribution)
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append(artist.artistName)
            }
            pop()
            append(" on ")
            pushStringAnnotation(tag = "unsplash attribution", annotation = unsplashAttribution)
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Unsplash")
            }
            pop()
        }
        Text(
            text = annotatedString,
            fontSize = 24.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
        ImageBox(
            image = image,
            imageType = "full",
            isClickable = false,
            onImageClick = onImageClick
        )
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
            image = mockImage/*FreshImage(
                id = "",
                title = "",
                urls = mapOf("" to ""),
                artistInfo = FreshImageArtist(
                    artistName = "",
                    links = mapOf("" to ""),
                    profileImage = mapOf("" to ""),
                )
            )*/,
            quote = listOf(
                FreshQuote(
                    quote = stringResource(id = R.string.placeholder_quote),
                    quoteAuthor = stringResource(id = R.string.placeholder_author),
                ),
            ),
            onQuoteClick = {},
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
            onQuoteClick = {},
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
