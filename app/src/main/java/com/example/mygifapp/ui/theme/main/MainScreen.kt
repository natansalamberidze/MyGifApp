package com.example.mygifapp.ui.theme.main

import android.content.res.Configuration
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
import com.example.mygifapp.R
import com.example.mygifapp.domain.model.GifItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import kotlinx.coroutines.flow.flowOf
import com.example.mygifapp.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onGifClick: (GifItem) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lazyGifItems = viewModel.gifsFlow.collectAsLazyPagingItems()

    MainScreenContent(
        searchQuery = searchQuery,
        lazyGifItems = lazyGifItems,
        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
        onGifClick = onGifClick
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    searchQuery: String,
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
                    modifier = Modifier.fillMaxWidth(0.88f),
                    placeholder = { Text(stringResource(id = R.string.search_gifs_hint)) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(focusedPlaceholderColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(spanCount),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(lazyGifItems.itemCount) { index ->
                    val gif = lazyGifItems[index]
                    GifGridItem(gif = gif!!, onClick = { onGifClick(gif) })
                }

                if (lazyGifItems.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(spanCount) }) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (lazyGifItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator()
            }

            if (lazyGifItems.loadState.refresh is LoadState.Error) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.error_message_hint),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { lazyGifItems.retry() }) {
                        Text(stringResource(id = R.string.retry_message_hint))
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
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // На реальном телефоне Glide отработает на 100% правильно
            GlideImage(
                model = gif.previewUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Preview(name = "Light theme", showBackground = true)
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    val fakeGifs = listOf(
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
        GifItem("", "", "", ""),
    )

    val fakePagingItems = flowOf(PagingData.from(fakeGifs)).collectAsLazyPagingItems()

    MyApplicationTheme {
        MainScreenContent(
            searchQuery = "Search GIFs",
            lazyGifItems = fakePagingItems,
            onSearchQueryChange = {},
            onGifClick = {}
        )
    }
}