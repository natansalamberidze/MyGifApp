package com.example.mygifapp.ui.theme.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygifapp.BuildConfig
import com.example.mygifapp.ui.theme.detail.DetailActivity
import com.example.mygifapp.R
import com.example.mygifapp.data.api.RetrofitInstance
import com.example.mygifapp.data.paging.GifPagingSource
import com.example.mygifapp.domain.model.GifItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainActivity : AppCompatActivity() {

    // Api key
    val myKey = BuildConfig.API_KEY
    private lateinit var adapter: GifAdapter

    private var searchJob: Job? = null

    private lateinit var mainLoader: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainLoader = findViewById(R.id.mainLoader)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchInput = findViewById<EditText>(R.id.searchInput)
        val errorTextMain = findViewById<TextView>(R.id.errorTextMain)

        adapter = GifAdapter { gif ->
            openDetails(gif)
        }

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        val gridLayoutManager = GridLayoutManager(this, spanCount)

        // Bottom loader
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && adapter.itemCount > 0) 2 else 1
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->

                val isLoading = loadStates.refresh is LoadState.Loading
                val isError = loadStates.refresh is LoadState.Error

                mainLoader.isVisible = isLoading
                errorTextMain.isVisible = isError

                if (isError) {
                    errorTextMain.setOnClickListener {
                        adapter.retry()
                    }
                }
            }
        }

        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = adapter.withLoadStateFooter(
            footer = GifLoadStateAdapter { adapter.retry() }
        )

        observeSearch(searchInput)
        search("cats")
    }

    // Searching
    fun observeSearch(searchInput: EditText) {
        val flow = callbackFlow {
            val listener = searchInput.doAfterTextChanged { text ->
                trySend(text.toString())
            }

            awaitClose {
                searchInput.removeTextChangedListener(listener)
            }
        }

        lifecycleScope.launch {
            flow
                .debounce(100)
                .map { it.trim() }
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .collectLatest { query ->
                    search(query)
                }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            getPager(query)
                .cachedIn(lifecycleScope)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun getPager(query: String): Flow<PagingData<GifItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GifPagingSource(
                    RetrofitInstance.api,
                    myKey,
                    query,
                    applicationContext
                )
            }
        ).flow
    }

    private fun openDetails(gif: GifItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("GIF_URL", gif.originalUrl)
        startActivity(intent)
    }
}