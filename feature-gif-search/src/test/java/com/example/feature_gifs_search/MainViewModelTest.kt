package com.example.feature_gifs_search

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.feature_gifs_search.data.repository.GifApiRepository
import com.example.feature_gifs_search.ui.main.MainViewModel
import com.example.network.util.NetworkMonitor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockRepository = mockk<GifApiRepository>(relaxed = true)
    private val mockNetworkMonitor = mockk<NetworkMonitor>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockRepository.searchGifs(any()) } returns flowOf(PagingData.Companion.empty())
        every { mockNetworkMonitor.isConnected } returns flowOf(true)
        viewModel = MainViewModel(
            repository = mockRepository,
            networkMonitor = mockNetworkMonitor
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial search query should be empty`() {
        Assert.assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `onSearchQueryChanged should update searchQuery state`() {
        viewModel.onSearchQueryChanged("Cats")
        Assert.assertEquals("Cats", viewModel.searchQuery.value)
    }

    @Test
    fun `searchQuery updates should be emitted sequentially`() =
        runTest(UnconfinedTestDispatcher()) {
            viewModel.searchQuery.test {
                Assert.assertEquals("", awaitItem())

                viewModel.onSearchQueryChanged("Cats")
                Assert.assertEquals("Cats", awaitItem())

                viewModel.onSearchQueryChanged("Dogs")
                Assert.assertEquals("Dogs", awaitItem())
            }
        }

    @Test
    fun `gifsFlow should trigger repository search only after debounce time`() =
        runTest(testDispatcher) {
            viewModel.gifsFlow.test {
                viewModel.onSearchQueryChanged("Cats")

                advanceTimeBy(200)
                verify(exactly = 0) { mockRepository.searchGifs("Cats") as Any }

                advanceTimeBy(301)
                verify(exactly = 1) { mockRepository.searchGifs("Cats") as Any }

                cancelAndIgnoreRemainingEvents()
            }
        }
}