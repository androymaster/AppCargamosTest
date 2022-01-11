package com.example.appcargamostest.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.appcargamostest.R
import com.example.appcargamostest.data.local.AppDatabase
import com.example.appcargamostest.data.local.LocalMovieDataSource
import com.example.appcargamostest.data.model.toFavoriteMovie
import com.example.appcargamostest.databinding.FragmentDetailBinding
import com.example.appcargamostest.presentation.FavoriteMovieViewModel
import com.example.appcargamostest.presentation.FavoriteMovieViewModelFactory
import com.example.appcargamostest.ui.main.MovieAndSeriesFragmentDirections

class DetailFragment : DialogFragment() {

    private var isFavorite = false
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel by viewModels<FavoriteMovieViewModel> {
        FavoriteMovieViewModelFactory(
            LocalMovieDataSource(
                AppDatabase.getDatabase(requireContext()).movieDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        findFavoriteMovie()

        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/w500/${args.backgroundImageUrl}").centerCrop().into(binding.imgMovie)
        binding.txtMovieTitle.text = args.title
        binding.txtDescription.text = args.overview

        binding.btnAddFavorite.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteFavoriteMovie(args.toFavoriteMovie())
                    binding.btnAddFavorite.setImageResource(R.drawable.ic_favorite_border)
                    isFavorite = false
                } else {
                    viewModel.saveFavoriteMovie(args.toFavoriteMovie())
                }
            Toast.makeText(requireContext(),"Se guardo tu selección en favoritos", Toast.LENGTH_SHORT).show()
        }

        binding.viewFavorites.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToFavoritesMoviesFragment()
            findNavController().navigate(action)
        }
    }

    private fun findFavoriteMovie() {
        viewModel.favoriteMovieList.observe(viewLifecycleOwner, { movieList ->
            movieList.forEach {
                if (it.id == args.id) {
                    binding.btnAddFavorite.setImageResource(R.drawable.ic_favorite_white)
                    isFavorite = true
                    return@observe
                }
            }
        })
    }
}