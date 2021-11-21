package com.muhammadauliaadil.project.omdb.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadauliaadil.project.omdb.R
import com.muhammadauliaadil.project.omdb.data.SearchItem
import com.muhammadauliaadil.project.omdb.view.listener.PaginationListener
import com.muhammadauliaadil.project.omdb.view.listener.RecyclerItemClickListener
import com.muhammadauliaadil.project.omdb.viewmodel.MovieSearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START
    private var searchTerm: String = ""
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var searchView: SearchView
    private var searchResultsList: ArrayList<SearchItem> = ArrayList()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(searchResultsList)
    }
    private lateinit var movieSearchViewModel: MovieSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Omdb"
        initializeUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Search"
        searchView.isSubmitButtonEnabled = true
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(searchViewQueryTextListener)
        return true
    }

    private fun initializeUI() {
        movieSearchViewModel = MovieSearchViewModel(currentPage)
        layoutManager = GridLayoutManager(this@MainActivity, 2)
        movie_recycler_view.layoutManager = layoutManager
        movie_recycler_view.adapter = movieAdapter
        movie_recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(
                applicationContext,
                object :
                    RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val searchItem = searchResultsList[position]
                        val intent =
                            Intent(applicationContext, MovieDetailsActivity::class.java)
                        intent.putExtra("poster", searchItem.poster)
                        intent.putExtra("title", searchItem.title)
                        startActivity(intent)
                    }

                })
        )
        movie_recycler_view.addOnScrollListener(object : PaginationListener(layoutManager) {
            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                Handler().postDelayed({ loadNextPage(searchTerm) }, 1000)
            }
        })
        linear_layout.visibility = View.VISIBLE
        movie_recycler_view.visibility = View.GONE
    }

    private val searchViewQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            val queryLowerCase = query.toLowerCase(Locale.ROOT)
            isLoading = false
            isLastPage = false
            currentPage = PAGE_START
            movieSearchViewModel.searchResultsList.clear()
            getSearchResultMoviesData(queryLowerCase)
            searchView.onActionViewCollapsed()
            searchResultsList.clear()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    }

    fun getSearchResultMoviesData(query: String) {
        movie_recycler_view.visibility = View.VISIBLE
        linear_layout.visibility = View.GONE
        movie_recycler_view.showShimmer()
        movieSearchViewModel.getSearchResultMoviesData(query)
        movieSearchViewModel.getStatusResponse()?.observe(this, Observer {
            Log.d(
                "statusResponseDiActivity",
                movieSearchViewModel.getStatusResponse()?.value.orEmpty()
            )
            when (movieSearchViewModel.getStatusResponse()?.value) {
                "SUCCESS_FOUND" -> {
                    searchResultsList.addAll(movieSearchViewModel.searchResultsList)
                    movie_recycler_view.hideShimmer()
                    if (currentPage <= TOTAL_PAGES) {
                        movieAdapter.addLoadingFooter()
                    } else {
                        isLastPage = true
                    }
                }
                "SUCCESS_NOT_FOUND" -> {
                    movie_recycler_view.visibility = View.GONE
                    linear_layout.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, "Too many results!", Toast.LENGTH_SHORT)
                        .show()
                }
                "SUCCESS_NOT_RESPONSE" -> {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
                "FAILURE" -> {
                    movie_recycler_view.hideShimmer()
                    Toast.makeText(
                        applicationContext,
                        "Loading API has failed. Please enable your internet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun loadNextPage(query: String) {
        movieSearchViewModel.loadNextPage(query)
        movieSearchViewModel.getStatusResponse()?.observe(this, Observer {
            Log.d(
                "statusResponseDiActivity",
                movieSearchViewModel.getStatusResponse()?.value.orEmpty()
            )
            when (movieSearchViewModel.getStatusResponse()?.value) {
                "SUCCESS_FOUND" -> {
                    movieAdapter.removeLoadingFooter()
                    isLoading = false
                    searchResultsList.addAll(movieSearchViewModel.searchResultsList)
                    if (currentPage != TOTAL_PAGES)
                        movieAdapter.addLoadingFooter()
                    else
                        isLastPage = true
                }
                "SUCCESS_NOT_FOUND" -> {
                    isLoading = false
                    isLastPage = true
                    movieAdapter.removeLoadingFooter()
                    Toast.makeText(applicationContext, "Movie not found!", Toast.LENGTH_SHORT)
                        .show()
                }
                "SUCCESS_NOT_RESPONSE" -> {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
                "FAILURE" -> {
                    movie_recycler_view.hideShimmer()
                    Toast.makeText(
                        applicationContext,
                        "Loading API has failed. Please enable your internet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    companion object {
        private const val PAGE_START = 1
        private const val TOTAL_PAGES = 4
    }
}
