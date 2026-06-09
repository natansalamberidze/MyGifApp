package com.example.feature_gifs_search.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.feature_gifs_search.R
import com.example.feature_gifs_search.ui.theme.MyApplicationTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(gifUrl: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val imageModel: Any = if (LocalInspectionMode.current || gifUrl.isEmpty()) {
            R.drawable.n_letter
        } else {
            gifUrl
        }
        GlideImage(
            model = imageModel,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        ) { it.error(R.drawable.n_letter) }
    }
}

@Preview(name = "Light theme", showBackground = true)
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    MyApplicationTheme(dynamicColor = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailScreen(gifUrl = "")
        }
    }
}