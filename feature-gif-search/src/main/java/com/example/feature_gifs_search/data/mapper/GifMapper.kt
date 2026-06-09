package com.example.feature_gifs_search.data.mapper

import com.example.feature_gifs_search.model.GifItem
import com.example.network.model.GiphyResponse

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