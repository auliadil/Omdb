package com.muhammadauliaadil.project.omdb.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muhammadauliaadil.project.omdb.*
import com.muhammadauliaadil.project.omdb.data.Movie
import com.muhammadauliaadil.project.omdb.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var movieDetailsViewModel: MovieDetailsViewModel
    lateinit var movie : Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        movieDetailsViewModel = MovieDetailsViewModel()
        val intent= getIntent()
        toolbar_layout.title = intent.getStringExtra("title")
        Glide.with(applicationContext)
            .asBitmap()
            .thumbnail(0.5f)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .load(intent.getStringExtra("poster"))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(poster)
        card_view.visibility = View.GONE
        getMovieData(intent.getStringExtra("title").orEmpty())
    }

    fun getMovieData(query: String) {
        progressbar.visibility = View.VISIBLE
        movieDetailsViewModel.getMovieData(query)
        movieDetailsViewModel.statusResponse?.observe(this, Observer { statusResponse->
            Log.d("statusResponseDiActivity", movieDetailsViewModel.statusResponse?.value.orEmpty())
            when (movieDetailsViewModel.statusResponse?.value) {
                "SUCCESS_FOUND" -> {
                    movie = movieDetailsViewModel.movie
                    card_view.setVisibility(View.VISIBLE)
                    year.text = movie.year!!
                    runtime.text = movie.director
                    imdb_rating.setText(movie.imdbrating)
                    director.text = movie.director
                    writer.text = movie.writer
                    genre.text = movie.genre
                    plot.text = movie.plot
                }
                "SUCCESS_NOT_RESPONSE" -> {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
                "FAILURE" -> {
                    movie_recycler_view.hideShimmer()
                    Toast.makeText(applicationContext, "Loading API has failed. Please enable your internet", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}