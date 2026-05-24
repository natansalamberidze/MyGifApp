package com.example.mygifapp.ui.theme.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygifapp.R

class GifLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<GifLoadStateAdapter.LoaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loader, parent, false)
        return LoaderViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(
        view: View,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        private val errorText: TextView = view.findViewById(R.id.errorText)

        init {
            errorText.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Loading -> {
                    progressBar.isVisible = true
                    errorText.isVisible = false
                }
                is LoadState.Error -> {
                    progressBar.isVisible = false
                    errorText.isVisible = true
                }
                else -> {
                    progressBar.isVisible = false
                    errorText.isVisible = false
                }
            }
        }
    }
}