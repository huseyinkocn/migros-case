package com.example.news.ui.favorites

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
import com.example.news.R
import com.example.news.databinding.FragmentFavoritesBinding
import com.example.news.ui.adapter.ArticleAdapter
import com.example.news.ui.adapter.ArticleListItem
import com.example.news.util.gone
import com.example.news.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter(
            onItemClick = { article ->
                val bundle = Bundle().apply {
                    putInt("articleId", article.id)
                }
                findNavController().navigate(R.id.action_favorites_to_detail, bundle)
            },
            onFavoriteClick = { article ->
                viewModel.toggleFavorite(article)
            }
        )
        binding.recyclerView.adapter = articleAdapter
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorites.collect { favorites ->
                    val items = favorites.map { ArticleListItem.SmallArticle(it) }
                    articleAdapter.submitList(items)
                    if (favorites.isEmpty()) {
                        binding.emptyLayout.visible()
                        binding.recyclerView.gone()
                    } else {
                        binding.emptyLayout.gone()
                        binding.recyclerView.visible()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
