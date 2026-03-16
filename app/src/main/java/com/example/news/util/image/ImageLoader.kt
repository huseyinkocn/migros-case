package com.example.news.util.image

import android.content.Context
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
    private const val THUMBNAIL_FACTOR = 0.25f

    const val ITEM_VIEW_CACHE_SIZE = 10

    private val defaultOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.image_placeholder)

    private val thumbnailOptions = defaultOptions.clone()
        .centerCrop()
        .override(THUMBNAIL_SIZE)

    fun load(imageView: ImageView, url: String?) {
        Glide.with(imageView)
            .load(url)
            .apply(defaultOptions)
            .thumbnail(THUMBNAIL_FACTOR)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .centerCrop()
            .into(imageView)
    }

    fun loadThumbnail(imageView: ImageView, url: String?) {
        Glide.with(imageView)
            .load(url)
            .apply(thumbnailOptions)
            .thumbnail(THUMBNAIL_FACTOR)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
            .into(imageView)
    }

    fun preloadImages(context: Context, urls: List<String?>) {
        urls.filterNotNull().forEach { url ->
            Glide.with(context)
                .load(url)
                .apply(thumbnailOptions)
                .preload()
        }
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
