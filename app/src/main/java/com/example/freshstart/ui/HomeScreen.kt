package com.example.freshstart.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.freshstart.R
import com.example.freshstart.data.images.FreshImage
import com.example.freshstart.data.quotes.FreshQuote
import com.example.freshstart.ui.theme.FreshStartTheme

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
            isRefreshing = viewModel.isRefreshing
        )

        is HomeUiState.Error -> ErrorScreen(
            retryAction = viewModel::getFreshCard,//getRandomImage,
            modifier.fillMaxSize()
        )
    }
}

@Composable
fun FreshStartScreen(
    word: Int,
    image: FreshImage,
    quote: List<FreshQuote>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
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
            modifier = Modifier
        ) {
            FreshStartCard(
                //uiState = uiState,
                word, image, quote,
                modifier = Modifier,
                //contentPadding = contentPadding
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

// populated with the "fresh material" each refresh/day
@Composable
fun FreshStartCard(
    word: Int,
    image: FreshImage,//,
    quote: List<FreshQuote>,
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
            /*//Fresh Word
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = word),
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp
                )
            }*/
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
                    modifier = Modifier
                )
            }
            //Fresh Quote
            QuoteBox(
                quoteText = quote[0].quote,
                quoteAuthor = quote[0].quoteAuthor
            )
        }
    }

}

@Composable
fun ImageBox(
    image: FreshImage,
    //@StringRes contentDescription: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.urls["regular"])
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
fun QuoteBox(
    quoteText: String,
    quoteAuthor: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = quoteText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 32.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.5f))
            Text(
                text = "- $quoteAuthor",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 24.sp
            )
        }

    }
}

@Preview
@Composable
fun FreshStartCardPreview() {
    FreshStartTheme {
        FreshStartCard(
            word = R.string.placeholder_word,
            image = FreshImage(
                id = "",
                title = "",
                urls = mapOf("" to ""),
            ),
            quote = listOf(
                FreshQuote(
                    quote = stringResource(id = R.string.placeholder_quote),
                    quoteAuthor = stringResource(id = R.string.placeholder_author)
                )
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    FreshStartTheme {
        FreshStartCard(
            word = R.string.placeholder_word,
            image = FreshImage(
                id = "",
                title = "",
                urls = mapOf("" to ""),
            ),
            quote = listOf(
                FreshQuote(
                    quote = stringResource(id = R.string.placeholder_quote),
                    quoteAuthor = stringResource(id = R.string.placeholder_author)
                )
            ),
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
