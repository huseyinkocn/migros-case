package com.example.news.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.news.R
import com.example.news.databinding.FragmentArticleDetailBinding
import com.example.news.ui.base.BaseFragment
import com.example.news.util.extension.launchAndRepeatWithViewLifecycle
import com.example.news.util.extension.toRelativeTime
import com.example.news.util.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ArticleDetailFragment : BaseFragment<FragmentArticleDetailBinding, ArticleDetailViewModel>(
    layoutResId = R.layout.fragment_article_detail
) {

    override val binding: FragmentArticleDetailBinding by viewBinding(FragmentArticleDetailBinding::bind)
    override val viewModel: ArticleDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        observers()
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.onAction(ArticleDetailContract.ArticleDetailAction.onLoadArticle)
        }

        binding.ivLike.setOnClickListener {
            viewModel.onAction(ArticleDetailContract.ArticleDetailAction.onFavoriteClick)
        }

        binding.ivBookMark.setOnClickListener {
            viewModel.onAction(ArticleDetailContract.ArticleDetailAction.onFavoriteClick)
        }
    }

    private fun observers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.viewState.collectLatest { viewState ->
                with(binding) {
                    btnFollowing.text = getString(viewState.following)
                    btnRetry.text = getString(viewState.btnRetry)
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.state.collectLatest { state ->
                with(binding) {

                    binding.tvDetailTitle.text = state.article.title
                    binding.tvDetailSummary.text = state.article.summary
                    binding.tvDetailDate.text = state.article.publishedAt.toRelativeTime()
                    binding.tvDetailNewsSite.text = state.article.newsSite
                    binding.tvDetailTag.text = state.article.newsSite

                    Glide.with(this@ArticleDetailFragment)
                        .load(state.article.imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                        .centerCrop()
                        .into(binding.ivDetailImage)

                    binding.btnMore.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, state.article.title)
                            putExtra(Intent.EXTRA_TEXT, "${state.article.title}\n\n${state.article.url}")
                        }
                        startActivity(
                            Intent.createChooser(
                                shareIntent,
                                getString(R.string.share)
                            )
                        )
                    }

                    binding.btnFollowing.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(state.article.url))
                        startActivity(browserIntent)
                    }

                    binding.ivLike.setImageResource(
                        if (state.article.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                    binding.ivBookMark.setImageResource(
                        if (state.article.isFavorite) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
                    )
                }
            }
        }
    }
}
