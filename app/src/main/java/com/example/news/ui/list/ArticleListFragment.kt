package com.example.news.ui.list


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentArticleListBinding
import com.example.news.domain.model.ArticleListItem
import com.example.news.domain.model.ArticleUiModel
import com.example.news.ui.adapter.ArticleAdapter
import com.example.news.ui.base.BaseFragment
import com.example.news.ui.favorites.ARTICLE_ID_KEY
import com.example.news.util.Resource
import com.example.news.util.extension.gone
import com.example.news.util.extension.launchAndRepeatWithViewLifecycle
import com.example.news.util.extension.viewBinding
import com.example.news.util.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ArticleListFragment : BaseFragment<FragmentArticleListBinding, ArticleListViewModel>(
    layoutResId = R.layout.fragment_article_list
) {

    override val binding: FragmentArticleListBinding by viewBinding(FragmentArticleListBinding::bind)

    override val viewModel: ArticleListViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListeners()
        observers()
    }

    private fun initRecyclerView() {
        articleAdapter = ArticleAdapter(
            onItemClick = { article ->
                viewModel.onAction(ArticleListContract.ArticleListAction.onItemClick(article))
            },
            onFavoriteClick = { article ->
                viewModel.onAction(ArticleListContract.ArticleListAction.onAddFavoriteClick(article))
            }
        )
        binding.rvNewsList.adapter = articleAdapter
    }

    private fun initListeners() = with(binding) {
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

        binding.btnRetry.setOnClickListener {
            viewModel.loadArticles()
        }
    }

    private fun observers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.viewState.collectLatest { viewState ->
                with(binding) {
                    btnRetry.text = getString(viewState.btnRetry)
                    searchView.hint = getString(viewState.searchHint)
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.state.collectLatest { state ->
                with(binding) {
                    when (state.article) {
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
                            val listItems = buildListItems(state.article.data)
                            articleAdapter.submitList(listItems)
                        }

                        is Resource.Error -> {
                            binding.lottieLoading.gone()
                            binding.lottieLoading.cancelAnimation()
                            binding.rvNewsList.gone()
                            binding.errorLayout.visible()
                            binding.tvError.text = state.article.message
                        }
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is ArticleListContract.ArticleListEffect.ItemClick -> {
                        val bundle = Bundle().apply {
                            putInt(ARTICLE_ID_KEY, effect.article.id)
                        }
                        findNavController().navigate(R.id.action_list_to_detail, bundle)
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
