package com.thiagoseiji.movieapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.thiagoseiji.movieapp.ui.adapters.MoviesAdapter
import com.thiagoseiji.movieapp.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import com.thiagoseiji.movieapp.R
import com.thiagoseiji.movieapp.ui.listeners.MovieListListener


class MainActivity : AppCompatActivity(), MovieListListener {


    private val moviesVM: MoviesViewModel by viewModel()

    private val moviesAdapter: MoviesAdapter by lazy { MoviesAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_recycler_view.adapter = moviesAdapter
        main_recycler_view.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.columns))

        val dividerItemDecoration = DividerItemDecoration(this,
            GridLayoutManager.HORIZONTAL
        )
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_horizontal))
        val dividerItemDecoration2 = DividerItemDecoration(this,
            GridLayoutManager.VERTICAL
        )
        dividerItemDecoration2.setDrawable(resources.getDrawable(R.drawable.divider_vertical))
        main_recycler_view.addItemDecoration(dividerItemDecoration)
        main_recycler_view.addItemDecoration(dividerItemDecoration2)

       getPopularMovies()

        main_upcoming.setOnClickListener {
            getUpcomingMovies()
        }

        main_popular.setOnClickListener {
            getPopularMovies()
        }
    }

    override fun onMovieClicked(id: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    //TODO refactor code to use just one observer
    fun getUpcomingMovies(){
        moviesVM.getUpcomingMovies().observe(this, Observer { data ->
            if (data != null) {
                moviesAdapter.updateMovieList(data.results)
            }
        })
    }

    fun getPopularMovies(){
        moviesVM.getPopularMovies().observe(this, Observer { data ->
            if (data != null) {
                moviesAdapter.updateMovieList(data.results)
            }
        })
    }
}
