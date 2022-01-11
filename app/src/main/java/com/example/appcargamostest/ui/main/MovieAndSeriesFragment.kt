package com.example.appcargamostest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.bumptech.glide.Glide
import com.example.appcargamostest.R
import com.example.appcargamostest.core.Resource
import com.example.appcargamostest.data.local.AppDatabase
import com.example.appcargamostest.data.local.LocalMovieDataSource
import com.example.appcargamostest.data.model.Movie
import com.example.appcargamostest.data.remote.RemoteMovieDataSource
import com.example.appcargamostest.databinding.FragmentDetailBinding
import com.example.appcargamostest.databinding.FragmentMovieAndSeriesBinding
import com.example.appcargamostest.domain.MovieRepositoryImpl
import com.example.appcargamostest.domain.RetrofitClient
import com.example.appcargamostest.presentation.MovieViewModel
import com.example.appcargamostest.presentation.MovieViewModelFactory
import com.example.appcargamostest.ui.main.adapters.MoviesAdapter
import com.example.appcargamostest.ui.main.adapters.concat.RecentMoviesConcatAdapter
import com.example.appcargamostest.ui.main.adapters.concat.RecentSeriesConcatAdapter

class MovieAndSeriesFragment : Fragment(R.layout.fragment_movie_and_series), MoviesAdapter.OnMovieClickListener {

    private lateinit var concatAdapter: ConcatAdapter
    private var _binding: FragmentMovieAndSeriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                RemoteMovieDataSource(RetrofitClient.webservice),
                LocalMovieDataSource(AppDatabase.getDatabase(requireContext()).movieDao())
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieAndSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(
                            0,
                            RecentMoviesConcatAdapter(
                                MoviesAdapter(
                                    it.data.first.results,
                                    this@MovieAndSeriesFragment
                                )
                            )
                        )
                        addAdapter(
                            1,
                            RecentSeriesConcatAdapter(
                                MoviesAdapter(
                                    it.data.second.results,
                                    this@MovieAndSeriesFragment
                                )
                            )
                        )
                    }
                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    //Log.e("FetchError", "Error: $it.exception ")
                    Toast.makeText(requireContext(), "Error: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieAndSeriesFragmentDirections.actionMovieAndSeriesFragmentToDetailFragment(
            movie.backdrop_path!!,
            movie.title,
            movie.overview,
            movie.poster_path,
            movie.id,
            movie.original_language,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.release_date,
        )
        findNavController().navigate(action)
    }
}