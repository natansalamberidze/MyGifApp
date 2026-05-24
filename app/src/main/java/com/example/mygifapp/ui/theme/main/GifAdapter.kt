package com.example.mygifapp.ui.theme.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygifapp.R
import com.example.mygifapp.domain.model.GifItem

class GifAdapter (private val onClick: (GifItem) -> Unit) :
    PagingDataAdapter<GifItem, GifAdapter.GifViewHolder>(DIFF_CALLBACK) {

     inner class GifViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.imageView)

        fun bind(item: GifItem) {
            Glide.with(image)
                .asGif()
                .load(item.previewUrl)
                .into(image)

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gif, parent, false)
        return GifViewHolder(view)

    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        internal val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GifItem>() {
            override fun areItemsTheSame(oldItem: GifItem, newItem: GifItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GifItem, newItem: GifItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}