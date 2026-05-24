package com.example.mygifapp

import org.junit.Test
import androidx.recyclerview.widget.DiffUtil
import com.example.mygifapp.domain.model.GifItem
import com.example.mygifapp.ui.theme.main.GifAdapter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before

class GifAdapterDiffTest {
    private lateinit var diffCallback: DiffUtil.ItemCallback<GifItem>

    @Before
    fun setUp() {
        diffCallback = GifAdapter.DIFF_CALLBACK
    }

    @Test
    fun `areItemsTheSame returns true for items with identical IDs`() {
        val oldItem = GifItem(id = "123", url = "https://sitex.com", previewUrl = "https://old-p.gif", originalUrl = "https://old-o.gif")
        val newItem = GifItem(id = "123", url = "https://sitey.com", previewUrl = "https://new-p.gif", originalUrl = "https://new-o.gif")

        val result = diffCallback.areItemsTheSame(oldItem, newItem)
        assertTrue(result)
    }

    @Test
    fun `areItemsTheSame returns false for items with different IDs`() {
        val oldItem = GifItem(id = "123", url = "https://site.com", previewUrl = "https://p.gif", originalUrl = "https://o.gif")
        val newItem = GifItem(id = "456", url = "https://site.com", previewUrl = "https://p.gif", originalUrl = "https://o.gif")

        val result = diffCallback.areItemsTheSame(oldItem, newItem)
        assertFalse(result)
    }

    @Test
    fun `areContentsTheSame returns true for absolutely identical items`() {
        val oldItem = GifItem(id = "123", url = "https://site.com", previewUrl = "https://p.gif", originalUrl = "https://o.gif")
        val newItem = GifItem(id = "123", url = "https://site.com", previewUrl = "https://p.gif", originalUrl = "https://o.gif")

        val result = diffCallback.areContentsTheSame(oldItem, newItem)
        assertTrue(result)
    }

    @Test
    fun `areContentsTheSame returns false if any field is changed`() {
        val oldItem = GifItem(id = "123", url = "https://site.com", previewUrl = "https://old-p.gif", originalUrl = "https://o.gif")
        val newItem = GifItem(id = "123", url = "https://site.com", previewUrl = "https://new-p.gif", originalUrl = "https://o.gif")

        val result = diffCallback.areContentsTheSame(oldItem, newItem)
        assertFalse(result)
    }
}