package com.example.mygifapp.data.model

data class GiphyResponse(
    val data: List<GifDto>
)

data class GifDto(
    val id: String,
    val images: ImagesDto
)

data class ImagesDto(
    val original: OriginalDto,
    val fixed_width: FixedWidth
)

data class OriginalDto(
    val url: String
)

data class FixedWidth(
    val url: String
)