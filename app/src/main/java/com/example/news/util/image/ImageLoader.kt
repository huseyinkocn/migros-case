package com.example.news.util.image

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.news.R

object ImageLoader {

    private const val CROSSFADE_DURATION = 150
    private const val THUMBNAIL_SIZE = 288

    const val ITEM_VIEW_CACHE_SIZE = 10

    private val defaultOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    private val thumbnailOptions = defaultOptions.clone()
        .centerCrop()
        .override(THUMBNAIL_SIZE)

    fun load(imageView: ImageView, url: String?) {
        Glide.with(imageView)
            .load(url)
            .apply(defaultOptions)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .centerCrop()
            .into(imageView)
    }

    fun loadThumbnail(imageView: ImageView, url: String?) {
        Glide.with(imageView)
            .load(url)
            .apply(thumbnailOptions)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .into(imageView)
    }

    fun createScrollPauseListener(fragment: Fragment) = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING,
                RecyclerView.SCROLL_STATE_SETTLING -> Glide.with(fragment).pauseRequests()
                RecyclerView.SCROLL_STATE_IDLE -> Glide.with(fragment).resumeRequests()
            }
        }
    }
}
