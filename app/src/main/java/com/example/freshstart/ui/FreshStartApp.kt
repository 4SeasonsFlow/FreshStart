package com.example.freshstart.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshstart.R

@Composable
fun FreshStartApp() {
    val homeScreenViewModel: HomeScreenViewModel =
        viewModel(factory = HomeScreenViewModel.Factory)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { FreshStartTopAppBar() },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            HomeScreen(
                viewModel = homeScreenViewModel,
                modifier = Modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreshStartTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) }
    )
}