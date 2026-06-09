package com.example.feature_gifs_search.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.feature_gifs_search.ui.detail.DetailScreen
import com.example.feature_gifs_search.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                var selectedGifUrl by rememberSaveable { mutableStateOf<String?>(null) }

                val currentUrl = selectedGifUrl

                if (currentUrl == null) {
                    MainScreen(
                        onGifClick = { gif ->
                            selectedGifUrl = gif.originalUrl
                        }
                    )
                } else {
                    BackHandler {
                        selectedGifUrl = null
                    }
                    DetailScreen(gifUrl = currentUrl)
                }
            }
        }
    }
}