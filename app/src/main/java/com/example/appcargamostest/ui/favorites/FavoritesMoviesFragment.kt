package com.example.appcargamostest.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.appcargamostest.data.local.AppDatabase
import com.example.appcargamostest.data.local.LocalMovieDataSource
import com.example.appcargamostest.data.model.FavoriteMovie
import com.example.appcargamostest.databinding.FragmentFavoritesMoviesBinding
import com.example.appcargamostest.presentation.FavoriteMovieViewModel
import com.example.appcargamostest.presentation.FavoriteMovieViewModelFactory
import com.example.appcargamostest.application.OnMovieClickListener
import java.text.FieldPosition

class FavoritesMoviesFragment : Fragment(), OnMovieClickListener<FavoriteMovie> {

    private var _binding: FragmentFavoritesMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var concatAdapter: FavoriteMovieAdapter
    private val viewModel by viewModels<FavoriteMovieViewModel> {
        FavoriteMovieViewModelFactory(
            LocalMovieDataSource(
                AppDatabase.getDatabase(requireContext()).movieDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        concatAdapter = FavoriteMovieAdapter(this)
        binding.rvFavoriteMovies.adapter = concatAdapter
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.favoriteMovieList.observe(viewLifecycleOwner, { movie ->
            if(movie.isEmpty()) {
                binding.rlNoFavorites.visibility = View.VISIBLE
            }
            concatAdapter.updateData(movie)
        })
    }

    override fun onMovieClick(movie: FavoriteMovie, position: Int) {
        viewModel.deleteFavoriteMovie(movie)
        binding.rvFavoriteMovies.adapter?.notifyItemRemoved(position)
    }
}