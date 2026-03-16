package com.example.news.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemArticleFeaturedBinding
import com.example.news.databinding.ItemArticleSmallBinding
import com.example.news.databinding.ItemSectionHeaderBinding
import com.example.news.domain.model.ArticleListItem
import com.example.news.domain.model.ArticleUiModel
import com.example.news.util.extension.toRelativeTime
import com.example.news.util.image.ImageLoader

private const val TYPE_HEADER = 0
private const val TYPE_FEATURED = 1
private const val TYPE_SMALL = 2

class ArticleAdapter(
    private val onItemClick: (ArticleUiModel) -> Unit
) : ListAdapter<ArticleListItem, RecyclerView.ViewHolder>(ArticleListDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArticleListItem.SectionHeader -> TYPE_HEADER
            is ArticleListItem.FeaturedArticle -> TYPE_FEATURED
            is ArticleListItem.SmallArticle -> TYPE_SMALL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> SectionHeaderViewHolder(
                ItemSectionHeaderBinding.inflate(inflater, parent, false)
            )
            TYPE_FEATURED -> FeaturedViewHolder(
                ItemArticleFeaturedBinding.inflate(inflater, parent, false)
            )
            TYPE_SMALL -> SmallViewHolder(
                ItemArticleSmallBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ArticleListItem.SectionHeader -> (holder as SectionHeaderViewHolder).bind(item)
            is ArticleListItem.FeaturedArticle -> (holder as FeaturedViewHolder).bind(item.article)
            is ArticleListItem.SmallArticle -> (holder as SmallViewHolder).bind(item.article)
        }
    }

    inner class SectionHeaderViewHolder(
        private val binding: ItemSectionHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleListItem.SectionHeader) {
            binding.tvSectionTitle.text = item.title
        }
    }

    inner class FeaturedViewHolder(
        private val binding: ItemArticleFeaturedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos) as? ArticleListItem.FeaturedArticle
                    item?.let { onItemClick(it.article) }
                }
            }
        }

        fun bind(article: ArticleUiModel) {
            binding.tvFeaturedTitle.text = article.title
            binding.tvFeaturedNewsSite.text = article.newsSite
            binding.tvFeaturedSource.text = article.newsSite
            binding.tvFeaturedDate.text = article.publishedAt.toRelativeTime()

            ImageLoader.load(binding.ivFeaturedImage, article.imageUrl)
        }
    }

    inner class SmallViewHolder(
        private val binding: ItemArticleSmallBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos) as? ArticleListItem.SmallArticle
                    item?.let { onItemClick(it.article) }
                }
            }
        }

        fun bind(article: ArticleUiModel) {
            binding.tvSmallTitle.text = article.title
            binding.tvSmallNewsSite.text = article.newsSite
            binding.tvSmallSource.text = article.newsSite
            binding.tvSmallDate.text = article.publishedAt.toRelativeTime()

            ImageLoader.loadThumbnail(binding.ivSmallImage, article.imageUrl)
        }
    }

    class ArticleListDiffCallback : DiffUtil.ItemCallback<ArticleListItem>() {
        override fun areItemsTheSame(oldItem: ArticleListItem, newItem: ArticleListItem): Boolean {
            return when {
                oldItem is ArticleListItem.SectionHeader && newItem is ArticleListItem.SectionHeader ->
                    oldItem.title == newItem.title
                oldItem is ArticleListItem.FeaturedArticle && newItem is ArticleListItem.FeaturedArticle ->
                    oldItem.article.id == newItem.article.id
                oldItem is ArticleListItem.SmallArticle && newItem is ArticleListItem.SmallArticle ->
                    oldItem.article.id == newItem.article.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ArticleListItem, newItem: ArticleListItem): Boolean {
            return oldItem == newItem
        }
    }
}
