package com.example.news.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentFavoritesBinding
import com.example.news.domain.model.ArticleListItem
import com.example.news.ui.adapter.ArticleAdapter
import com.example.news.ui.base.BaseFragment
import com.example.news.util.extension.gone
import com.example.news.util.extension.launchAndRepeatWithViewLifecycle
import com.example.news.util.extension.viewBinding
import com.example.news.util.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    layoutResId = R.layout.fragment_favorites
) {
    override val binding: FragmentFavoritesBinding by viewBinding(FragmentFavoritesBinding::bind)
    override val viewModel: FavoritesViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observers()
    }

    private fun initRecyclerView() {
        articleAdapter = ArticleAdapter(
            onItemClick = { article ->
                viewModel.onAction(FavoritesContract.FavoriteAction.onItemClick(article))
            },
            onFavoriteClick = { article ->
                viewModel.onAction(FavoritesContract.FavoriteAction.onAddFavoriteClick(article))
            }
        )
        binding.rvFavoriteList.adapter = articleAdapter
    }

    private fun observers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.viewState.collectLatest { viewState ->
                binding.tvTitle.text = getString(viewState.headerTitleRes)
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.state.collectLatest { state ->
                with(binding) {
                    val items = state.favoriteList.map { ArticleListItem.SmallArticle(it) }
                    articleAdapter.submitList(items)
                    if (state.favoriteList.isEmpty()) {
                        binding.emptyLayout.visible()
                        binding.rvFavoriteList.gone()
                    } else {
                        binding.emptyLayout.gone()
                        binding.rvFavoriteList.visible()
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is FavoritesContract.FavoriteEffect.ItemClick -> {
                        val bundle = Bundle().apply {
                            putInt(ARTICLE_ID_KEY, effect.article.id)
                        }
                        findNavController().navigate(R.id.action_favorites_to_detail, bundle)
                    }
                }
            }
        }
    }
}
