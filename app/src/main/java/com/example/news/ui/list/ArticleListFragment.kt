package com.example.news.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentArticleListBinding
import com.example.news.domain.model.Article
import com.example.news.ui.adapter.ArticleAdapter
import com.example.news.ui.adapter.ArticleListItem
import com.example.news.util.Resource
import com.example.news.util.gone
import com.example.news.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleListViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleListBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        binding.recyclerView.adapter = articleAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.onSearchQueryChanged(it) }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText.orEmpty())
                return true
            }
        })
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
                            binding.progressBar.visible()
                            binding.recyclerView.gone()
                            binding.errorLayout.gone()
                        }
                        is Resource.Success -> {
                            binding.progressBar.gone()
                            binding.errorLayout.gone()
                            binding.recyclerView.visible()
                            val listItems = buildListItems(resource.data)
                            articleAdapter.submitList(listItems)
                        }
                        is Resource.Error -> {
                            binding.progressBar.gone()
                            binding.recyclerView.gone()
                            binding.errorLayout.visible()
                            binding.tvError.text = resource.message
                        }
                    }
                }
            }
        }
    }

    private fun buildListItems(articles: List<Article>): List<ArticleListItem> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
