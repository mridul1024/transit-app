package com.zuk0.gaijinsmash.riderz.ui.fragment.favorite;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.databinding.ViewFavoritesBinding;
import com.zuk0.gaijinsmash.riderz.ui.adapter.favorite.FavoriteRecyclerAdapter;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.android.support.AndroidSupportInjection;

public class FavoritesFragment extends Fragment {

    @Inject
    FavoritesViewModelFactory mFavoritesViewModelFactory;

    private ViewFavoritesBinding mDataBinding;
    private FavoritesViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_favorites, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initFavorites();
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mFavoritesViewModelFactory).get(FavoritesViewModel.class);
    }

    private void initFavorites() {
        //todo: change bg color of priority to something else
        mViewModel.getFavorites().observe(this, data -> {
            if(data != null) {
                if(data.size() > 0) {
                    mDataBinding.bartFavoritesErrorTV.setVisibility(View.GONE);
                    FavoriteRecyclerAdapter adapter = new FavoriteRecyclerAdapter(data);
                    mDataBinding.bartFavoritesRecyclerView.setAdapter(adapter);
                    mDataBinding.bartFavoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });
    }
}
