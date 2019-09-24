package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentFavoritesBinding

import javax.inject.Inject

import androidx.recyclerview.widget.LinearLayoutManager
import com.zuk0.gaijinsmash.riderz.utils.StationUtils
import dagger.android.support.AndroidSupportInjection

class FavoritesFragment : Fragment() {

    @Inject
    lateinit var mFavoritesViewModelFactory: FavoritesViewModelFactory

    private lateinit var mDataBinding: FragmentFavoritesBinding
    private var mViewModel: FavoritesViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return mDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDagger()
        initViewModel()
        initFavorites()
        handleCardView()
    }

    private fun initDagger() {
        AndroidSupportInjection.inject(this)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this, mFavoritesViewModelFactory).get(FavoritesViewModel::class.java)
    }

    private fun initFavorites() {
        mViewModel?.favorites?.observe(this, Observer{ data ->
            if (data.isNotEmpty()) {
                mDataBinding.bartFavoritesCardView.visibility = View.GONE
                val adapter = FavoriteRecyclerAdapter(data, this)
                mDataBinding.bartFavoritesRecyclerView.adapter = adapter
                mDataBinding.bartFavoritesRecyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    private fun handleCardView() {
        mDataBinding.favoriteDepartAutoCompleteTextView
        mDataBinding.favoriteArrivalAutoCompleteTextView
        mDataBinding.button.setOnClickListener { v ->
            validate()
        }
    }

    private fun validate() {
        val depart = mDataBinding.favoriteDepartAutoCompleteTextView.text.toString()
        val arrive = mDataBinding.favoriteArrivalAutoCompleteTextView.text.toString()
        if(depart.isBlank()) {
            mDataBinding.favoriteDepartAutoCompleteTextView.error = resources.getString(R.string.error_field_incomplete)
            return
        }
        if(arrive.isBlank()) {
            mDataBinding.favoriteArrivalAutoCompleteTextView.error = resources.getString(R.string.error_field_incomplete)
            return
        }
        if(!StationUtils.validateStationName(depart)) {
            mDataBinding.favoriteDepartAutoCompleteTextView.error = resources.getString(R.string.error_station_not_found)
            return
        }
        if(!StationUtils.validateStationName(arrive)) {
            mDataBinding.favoriteArrivalAutoCompleteTextView.error = resources.getString(R.string.error_station_not_found)
            return
        }
        mViewModel?.saveFavorite(depart, arrive)
    }
}
