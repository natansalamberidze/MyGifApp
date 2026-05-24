package com.example.mygifapp.data.mapper

import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.data.model.GiphyResponse

fun mapToGifItems(response: GiphyResponse): List<GifItem> {
    return response.data.map {
        GifItem(
            id = it.id,
            url = it.images.original.url,
            previewUrl = it.images.fixed_width.url,
            originalUrl = it.images.original.url
        )
    }
}