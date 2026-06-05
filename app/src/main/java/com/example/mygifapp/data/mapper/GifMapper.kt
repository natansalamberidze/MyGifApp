package com.example.mygifapp.data.mapper

import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.data.model.GiphyResponse

fun GiphyResponse.toGifItems(): List<GifItem> {
    return data.map {
        GifItem(
            id = it.id,
            url = it.images.original.url,
            previewUrl = it.images.fixedWidth.url,
            originalUrl = it.images.original.url
        )
    }
}