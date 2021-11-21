package com.muhammadauliaadil.project.omdb.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muhammadauliaadil.project.omdb.R
import com.muhammadauliaadil.project.omdb.data.SearchItem

class MovieAdapter(private val searchResultsList: ArrayList<SearchItem>) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    internal lateinit var context: Context

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView? = null
        internal var year: TextView? = null
        internal var poster: ImageView? = null

        init {
            title = view.findViewById(R.id.title)
            year = view.findViewById(R.id.year)
            poster = view.findViewById(R.id.poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        var itemView: View? = null
        when (viewType) {
            ITEM -> itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_success, parent, false)
            LOADING -> itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress, parent, false)
        }
        context = parent.context
        return MovieHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val searchResults = searchResultsList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                holder.title?.setText(searchResults.title)
                holder.year?.setText(searchResults.year)
                holder.poster?.let {
                    Glide.with(context)
                        .asBitmap()
                        .centerCrop()
                        .load(searchResults.poster)
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(it)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (searchResultsList == null) 0 else searchResultsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == searchResultsList.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    fun add(r: SearchItem) {
        searchResultsList.add(r)
        notifyItemInserted(searchResultsList.size - 1)
    }

    fun remove(r: SearchItem?) {
        val position = searchResultsList.indexOf(r)
        if (position > -1) {
            searchResultsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(SearchItem())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = searchResultsList.size - 1
        val result = getItem(position)

        if (result != null) {
            searchResultsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): SearchItem? {
        return searchResultsList[position]
    }

}