package com.example.mygifapp.ui.theme.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mygifapp.R
import com.example.mygifapp.ui.theme.MyApplicationTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(gifUrl: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (gifUrl == R.drawable.n_letter.toString() || gifUrl.isEmpty()) {
            GlideImage(
                model = R.drawable.n_letter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            GlideImage(
                model = gifUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "Light theme", showBackground = true)
@Preview(name = "Dark theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    MyApplicationTheme {
        DetailScreen(gifUrl = R.drawable.n_letter.toString())
    }
}