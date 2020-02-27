package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentFavoritesBinding

import javax.inject.Inject

import androidx.recyclerview.widget.LinearLayoutManager
import com.zuk0.gaijinsmash.riderz.utils.StationUtils
import dagger.android.support.AndroidSupportInjection

class FavoritesFragment : Fragment() {

    @Inject
    lateinit var mFavoritesViewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initViewModel()
        initFavorites()
        handleCardView()
    }

    private fun initDagger() {
        AndroidSupportInjection.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, mFavoritesViewModelFactory).get(FavoritesViewModel::class.java)
    }

    private fun initFavorites() {
        viewModel.favorites?.observe(this, Observer{ data ->
            if (data.isNotEmpty()) {
                binding.bartFavoritesCardView.visibility = View.GONE
                val adapter = FavoriteRecyclerAdapter(data, this)
                binding.bartFavoritesRecyclerView.adapter = adapter
                binding.bartFavoritesRecyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    private fun handleCardView() {
        binding.favoriteDepartAutoCompleteTextView
        binding.favoriteArrivalAutoCompleteTextView
        binding.button.setOnClickListener { v ->
            validate()
        }
    }

    private fun validate() {
        val depart = binding.favoriteDepartAutoCompleteTextView.text.toString()
        val arrive = binding.favoriteArrivalAutoCompleteTextView.text.toString()
        if(depart.isBlank()) {
            binding.favoriteDepartAutoCompleteTextView.error = resources.getString(R.string.error_field_incomplete)
            return
        }
        if(arrive.isBlank()) {
            binding.favoriteArrivalAutoCompleteTextView.error = resources.getString(R.string.error_field_incomplete)
            return
        }
        if(!StationUtils.validateStationName(depart)) {
            binding.favoriteDepartAutoCompleteTextView.error = resources.getString(R.string.error_station_not_found)
            return
        }
        if(!StationUtils.validateStationName(arrive)) {
            binding.favoriteArrivalAutoCompleteTextView.error = resources.getString(R.string.error_station_not_found)
            return
        }
        viewModel.saveFavorite(depart, arrive)
    }
}
