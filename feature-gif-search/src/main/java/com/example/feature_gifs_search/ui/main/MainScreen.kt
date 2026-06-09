package com.example.feature_gifs_search.ui.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.feature_gifs_search.R
import com.example.feature_gifs_search.model.GifItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadStates
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf
import com.example.feature_gifs_search.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onGifClick: (GifItem) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lazyGifItems = viewModel.gifsFlow.collectAsLazyPagingItems()
    val isOffline by viewModel.isOffline.collectAsState()


    MainScreenContent(
        searchQuery = searchQuery,
        isOffline = isOffline,
        lazyGifItems = lazyGifItems,
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onGifClick = onGifClick
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    searchQuery: String,
    isOffline: Boolean,
    lazyGifItems: LazyPagingItems<GifItem>,
    onSearchQueryChange: (String) -> Unit,
    onGifClick: (GifItem) -> Unit
) {

    val configuration = LocalConfiguration.current
    val spanCount = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { onSearchQueryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth(0.88f),
                    placeholder = { Text(stringResource(id = R.string.search_gifs_hint)) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(focusedPlaceholderColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(spanCount),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(lazyGifItems.itemCount) { index ->
                    lazyGifItems[index]?.let { gif ->
                        GifGridItem(gif = gif, onClick = { onGifClick(gif) })
                    }
                }

                if (lazyGifItems.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(spanCount) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (lazyGifItems.loadState.append is LoadState.Error) {
                    item(span = { GridItemSpan(spanCount) }) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.error_message_hint),
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { lazyGifItems.retry() }) {
                                Text(stringResource(id = R.string.try_again_message_hint))
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isOffline && lazyGifItems.itemCount > 0,
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "No internet connection",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }

            if (lazyGifItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator()
            } else if (lazyGifItems.loadState.refresh is LoadState.Error) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.error_message_hint),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { lazyGifItems.retry() }) {
                        Text(stringResource(id = R.string.try_again_message_hint))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifGridItem(gif: GifItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(id = R.drawable.n_letter),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            GlideImage(
                model = gif.previewUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) { it.error(R.drawable.n_letter) }
        }
    }
}


@Preview(name = "Light theme", showBackground = true)
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    val fakeGifs = List(8) { GifItem(id = it.toString(), url = "", previewUrl = "", originalUrl = "") }

    val fakePagingItems = flowOf(
        PagingData.from(
            data = fakeGifs,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()

    MyApplicationTheme(dynamicColor = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreenContent(
                searchQuery = "Search GIFs",
                isOffline = true,
                lazyGifItems = fakePagingItems,
                onSearchQueryChange = {},
                onGifClick = {}
            )
        }
    }
}


@Preview(name = "Light theme (Refresh error screen)", showBackground = true)
@Preview(name = "Dark theme (Refresh error screen)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RefreshErrorPreview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.error_message_hint),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {}) {
                        Text(stringResource(id = R.string.try_again_message_hint))
                    }
                }
            }
        }
    }
}