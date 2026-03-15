package com.example.news.ui.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentArticleListBinding
import com.example.news.domain.model.ArticleListItem
import com.example.news.domain.model.ArticleUiModel
import com.example.news.ui.adapter.ArticleAdapter
import com.example.news.ui.base.BaseFragment
import com.example.news.util.Resource
import com.example.news.util.extension.gone
import com.example.news.util.extension.viewBinding
import com.example.news.util.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : BaseFragment<FragmentArticleListBinding, ArticleListViewModel>(
    layoutResId = R.layout.fragment_article_list
) {

    override val binding: FragmentArticleListBinding by viewBinding(FragmentArticleListBinding::bind)

    override val viewModel: ArticleListViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupRetryButton()
        observeArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter(
            onItemClick = { article ->
                val bundle = Bundle().apply {
                    putInt("articleId", article.id)
                }
                findNavController().navigate(R.id.action_list_to_detail, bundle)
            },
            onFavoriteClick = { article ->
                viewModel.toggleFavorite(article)
            }
        )
        binding.rvNewsList.adapter = articleAdapter
    }

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchView.clearFocus()
                true
            } else {
                false
            }
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadArticles()
        }
    }

    private fun observeArticles() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articles.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.lottieLoading.visible()
                            binding.lottieLoading.playAnimation()
                            binding.rvNewsList.gone()
                            binding.errorLayout.gone()
                        }
                        is Resource.Success -> {
                            binding.lottieLoading.gone()
                            binding.lottieLoading.cancelAnimation()
                            binding.errorLayout.gone()
                            binding.rvNewsList.visible()
                            val listItems = buildListItems(resource.data)
                            articleAdapter.submitList(listItems)
                        }
                        is Resource.Error -> {
                            binding.lottieLoading.gone()
                            binding.lottieLoading.cancelAnimation()
                            binding.rvNewsList.gone()
                            binding.errorLayout.visible()
                            binding.tvError.text = resource.message
                        }
                    }
                }
            }
        }
    }

    private fun buildListItems(articles: List<ArticleUiModel>): List<ArticleListItem> {
        if (articles.isEmpty()) return emptyList()

        val items = mutableListOf<ArticleListItem>()

        items.add(ArticleListItem.SectionHeader(getString(R.string.trending)))
        items.add(ArticleListItem.FeaturedArticle(articles.first()))

        if (articles.size > 1) {
            items.add(ArticleListItem.SectionHeader(getString(R.string.latest)))
            articles.drop(1).forEach { article ->
                items.add(ArticleListItem.SmallArticle(article))
            }
        }

        return items
    }
}
