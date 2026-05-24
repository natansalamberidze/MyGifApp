package com.example.mygifapp.data.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mygifapp.data.api.GiphyApi
import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.data.mapper.mapToGifItems
import com.example.mygifapp.utils.isInternetAvailable

class GifPagingSource(
    private val api: GiphyApi,
    private val apiKey: String,
    private val query: String,
    private val context: Context
) : PagingSource<Int, GifItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifItem> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            if (!isInternetAvailable(context)) {
                return LoadResult.Error(Exception("Check your internet connection"))
            }

            val response = api.searchGifs(
                apiKey = apiKey,
                query = query,
                limit = limit,
                offset = offset
            )

            val gifs = mapToGifItems(response)

            LoadResult.Page(
                data = gifs,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (gifs.isEmpty()) null else offset + limit
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifItem>): Int? {
        return state.anchorPosition
    }
}