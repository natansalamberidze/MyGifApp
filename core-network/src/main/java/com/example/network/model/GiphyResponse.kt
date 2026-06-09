package com.example.network.model

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    val data: List<GifDto>
)
data class GifDto(
    val id: String,
    val images: ImagesDto
)
data class ImagesDto(
    val original: OriginalDto,
    @SerializedName("fixed_width")
    val fixedWidth: FixedWidth
)
data class OriginalDto(
    val url: String
)
data class FixedWidth(
    val url: String
)