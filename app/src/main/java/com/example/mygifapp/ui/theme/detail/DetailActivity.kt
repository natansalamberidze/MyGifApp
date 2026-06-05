package com.example.mygifapp.ui.theme.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mygifapp.ui.theme.MyApplicationTheme

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("GIF_URL") ?: ""

        setContent {
            MyApplicationTheme {
                DetailScreen(gifUrl = url)
            }
        }
    }
}