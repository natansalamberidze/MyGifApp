package com.example.feature_gifs_search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.feature_gifs_search.data.paging.GifPagingSource
import com.example.feature_gifs_search.model.GifItem
import com.example.network.api.GiphyApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GifApiRepository @Inject constructor(
    private val api: GiphyApi
) {
    fun searchGifs(query: String): Flow<PagingData<GifItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GifPagingSource(api, query)
            }
        ).flow
    }
}