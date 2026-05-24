package com.example.mygifapp.ui.theme.detail

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mygifapp.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageView = findViewById<ImageView>(R.id.detailImage)

        val url = intent.getStringExtra("GIF_URL")

        Glide.with(this)
            .asGif()
            .load(url)
            .into(imageView)
    }
}