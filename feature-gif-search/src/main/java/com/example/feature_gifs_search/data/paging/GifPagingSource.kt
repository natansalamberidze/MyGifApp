package com.example.feature_gifs_search.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.feature_gifs_search.model.GifItem
import com.example.network.api.GiphyApi
import com.example.feature_gifs_search.data.mapper.toGifItems
import retrofit2.HttpException
import java.io.IOException

class GifPagingSource(
    private val api: GiphyApi,
    private val query: String,
) : PagingSource<Int, GifItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifItem> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            val response = if (query.isEmpty()) {
                api.getTrendingGifs(
                    limit = limit,
                    offset = offset
                )
            } else {
                api.searchGifs(
                    query = query,
                    limit = limit,
                    offset = offset
                )
            }

            val gifs = response.toGifItems()

            LoadResult.Page(
                data = gifs,
                prevKey = (offset - limit).takeUnless { offset == 0 },
                nextKey = if (gifs.isEmpty()) null else offset + limit
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize)
                ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}