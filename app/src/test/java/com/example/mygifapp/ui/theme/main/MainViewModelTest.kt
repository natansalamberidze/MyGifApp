package com.example.mygifapp.ui.theme.main

import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockApi: com.example.mygifapp.data.api.GiphyApi = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(
            api = mockApi,
            apiKey = "test_api_key_123"
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `initial search query should be empty`() = runTest {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `onSearchQueryChanged should update searchQuery state`() = runTest {
        val expectedQuery = "Funny Cats"

        viewModel.onSearchQueryChanged(expectedQuery)
        assertEquals(expectedQuery, viewModel.searchQuery.value)
    }

    @Test
    fun `searchQuery updates should be emitted sequentially`() = runTest {
        viewModel.searchQuery.test {
            assertEquals("", awaitItem())

            viewModel.onSearchQueryChanged("Dog")
            assertEquals("Dog", awaitItem())
            
            viewModel.onSearchQueryChanged("Car")
            assertEquals("Car", awaitItem())
        }
    }
}