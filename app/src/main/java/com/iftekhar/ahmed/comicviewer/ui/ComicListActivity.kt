package com.iftekhar.ahmed.comicviewer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.iftekhar.ahmed.comicviewer.R
import com.iftekhar.ahmed.comicviewer.databinding.ActivityComicListBinding
import com.iftekhar.ahmed.comicviewer.util.EmptyStatePresenter
import com.iftekhar.ahmed.comicviewer.viewmodel.SearchViewModel
import com.michaelfotiads.xkcdreader.data.db.entity.ComicEntity
import snackRetry
import toast

class ComicListActivity : BaseActivity(), EmptyStatePresenter {
    override fun getEmptyStateLayout() = binding.includeLayout
    private var lastQuery: String? = null
    lateinit var binding: ActivityComicListBinding
    private val viewModel by viewModels<SearchViewModel>()
    lateinit var adapter: ComicAdapter
    val needSearch: Boolean by lazy { intent.getBooleanExtra("needSearch", false) }

    companion object {
        fun start(context: Context, needSearch: Boolean) {
            val starter = Intent(context, ComicListActivity::class.java)
            starter.putExtra("needSearch", needSearch)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initViews()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (needSearch) {
            showEmptyStat("Search comics by number or text")
            supportActionBar?.title = "Search Comics"
        } else {
            supportActionBar?.title = "Favorites Comics"
        }
        binding.layoutSwipeRefresh.setOnRefreshListener { loadData()}

    }

    override fun onStart() {
        super.onStart()
        if (needSearch.not()) {
            loadData()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!needSearch) return false
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val mSearchView = item.actionView as SearchView
        mSearchView.setIconifiedByDefault(false)
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                lastQuery = query
                loadData()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }


    private fun initViews() {
        viewModel.comicsData.observe(this, { result ->
            result.onSuccess { list ->
                binding.layoutSwipeRefresh.isRefreshing = false
                if (list.isNotEmpty()) {
                    showContent()
                } else {
                    if (needSearch) {
                        showEmptyStat("No Comics matches your search query")
                    } else {
                        showEmptyStat("No Comics in your favorites")
                    }

                }
                adapter.update(list)
            }.onFailure {
                binding.layoutSwipeRefresh.isRefreshing = false
                if (adapter.itemCount > 0) {
                    snackRetry(it.message) { loadData() }
                } else {
                    showNetworkError { loadData() }
                    toast(it.message)
                }
            }
        })
    }

    private fun loadData() {
        binding.layoutSwipeRefresh.isRefreshing = true
        if (needSearch == true && lastQuery.isNullOrEmpty().not()) {
            viewModel.searchComics(needSearch, lastQuery!!)
        } else {
            viewModel.searchComics(needSearch, "")
        }


    }

    private fun initAdapter() {
        adapter = ComicAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnItemCLickListener {
            ComicDetailActivity.start(this, it)
        }
    }

}

