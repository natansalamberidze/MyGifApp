package com.example.mygifapp

import android.content.Context
import androidx.paging.PagingSource
import com.example.mygifapp.data.api.GiphyApi
import com.example.mygifapp.data.model.FixedWidth
import com.example.mygifapp.data.model.GifDto
import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.data.model.GiphyResponse
import com.example.mygifapp.data.model.ImagesDto
import com.example.mygifapp.data.model.OriginalDto
import com.example.mygifapp.data.paging.GifPagingSource
import com.example.mygifapp.utils.isInternetAvailable
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GifPagingSourceTest {

    // Stubs for the GifPagingSource builder
    private val mockApi = mockk<GiphyApi>()
    private val mockContext = mockk<Context>(relaxed = true)

    private val apiKey = "test_api_key"
    private val query = "alias"

    private lateinit var pagingSource: GifPagingSource

    @Before
    fun setUp() {
        // Mocking the static function that checks for an internet connection
        mockkStatic("com.example.myapplication.utils.NetworkUtilsKt")

        // By default, our tests assume that an internet connection IS available
        every { isInternetAvailable(any()) } returns true

        // Initializing the object under test
        pagingSource = GifPagingSource(mockApi, apiKey, query, mockContext)
    }

    @After
    fun tearDown() {
        // Clearing the static mocks after each test
        unmockkStatic("com.example.myapplication.utils.NetworkUtilsKt")
    }

    @Test
    fun `load returns LoadResult Page when network call is successful`() = runTest {
        // Arrange: Creating a real server response structure, GiphyResponse
        val fakeGiphyResponse = GiphyResponse(
            data = listOf(
                GifDto(
                    id = "123",
                    images = ImagesDto(
                        original = OriginalDto(url = "https://original.gif"),
                        fixed_width = FixedWidth(url = "https://preview.gif")
                    )
                )
            )
        )

        // Return our prepared fakeGiphyResponse
        coEvery { mockApi.searchGifs(apiKey, query, limit = 10, offset = 0) } returns fakeGiphyResponse

        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null, // First page (offset = 0)
            loadSize = 10,
            placeholdersEnabled = false
        )

        // Act: Running the `load` method
        val result = pagingSource.load(params)

        // Assert: Checking that the result is successful
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, GifItem>

        // Checking that the mapper has worked correctly and converted the DTO into GifItem objects
        assertEquals(1, page.data.size)
        assertEquals("123", page.data[0].id)
        assertEquals("https://preview.gif", page.data[0].previewUrl)
        assertEquals("https://original.gif", page.data[0].originalUrl)

        // Checking the pagination keys
        assertEquals(null, page.prevKey)
        assertEquals(10, page.nextKey) // offset(0) + limit(10) = 10
    }

    @Test
    fun `load returns LoadResult Error when internet is not available`() = runTest {
        // Arrange: Настраиваем утилиту так, будто интернета НЕТ
        every { isInternetAvailable(any()) } returns false

        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 10,
            placeholdersEnabled = false
        )

        // Act: Running the `load` method
        val result = pagingSource.load(params)

        // Assert: Ensure that the server error is correctly handled by the paginator
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals("Check your internet connection", errorResult.throwable.message)
    }

    @Test
    fun `load returns LoadResult Error when API throws exception`() = runTest {
        // Arrange: Simulate a Retrofit network error (server unavailable)
        coEvery { mockApi.searchGifs(any(), any(), any(), any()) } throws RuntimeException("Server Error")

        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 10,
            placeholdersEnabled = false
        )

        // Act: Running the `load` method
        val result = pagingSource.load(params)

        // Assert: Ensure that the server error is correctly handled by the paginator
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals("Server Error", errorResult.throwable.message)
    }
}