package com.example.mygifapp.ui.theme.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.ui.theme.MyApplicationTheme
import com.example.mygifapp.ui.theme.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                MainScreen(
                    viewModel = viewModel,
                    onGifClick = ::openDetails
                )
            }
        }
    }

    private fun openDetails(gif: GifItem) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("GIF_URL", gif.originalUrl)
        }
        startActivity(intent)
    }
}