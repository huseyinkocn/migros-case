package com.example.news.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.news.R
import com.example.news.databinding.FragmentArticleDetailBinding
import com.example.news.util.Resource
import com.example.news.util.gone
import com.example.news.util.toRelativeTime
import com.example.news.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        observeArticle()
        observeFavoriteStatus()
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadArticle()
        }

        binding.ivLike.setOnClickListener {
            viewModel.toggleFavorite()
        }

        binding.ivBookmark.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun observeArticle() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.article.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visible()
                            binding.scrollView.gone()
                            binding.bottomBar.gone()
                            binding.errorLayout.gone()
                        }
                        is Resource.Success -> {
                            binding.progressBar.gone()
                            binding.errorLayout.gone()
                            binding.scrollView.visible()
                            binding.bottomBar.visible()

                            val article = resource.data
                            binding.tvDetailTitle.text = article.title
                            binding.tvDetailSummary.text = article.summary
                            binding.tvDetailDate.text = article.publishedAt.toRelativeTime()
                            binding.tvDetailNewsSite.text = article.newsSite
                            binding.tvDetailTag.text = article.newsSite

                            Glide.with(this@ArticleDetailFragment)
                                .load(article.imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                                .centerCrop()
                                .into(binding.ivDetailImage)

                            binding.btnMore.setOnClickListener {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, article.title)
                                    putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                                }
                                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
                            }

                            binding.btnFollowing.setOnClickListener {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                startActivity(browserIntent)
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.gone()
                            binding.scrollView.gone()
                            binding.bottomBar.gone()
                            binding.errorLayout.visible()
                            binding.tvError.text = resource.message
                        }
                    }
                }
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { isFav ->
                    binding.ivLike.setImageResource(
                        if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                    binding.ivBookmark.setImageResource(
                        if (isFav) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
