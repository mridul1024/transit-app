package com.zuk0.gaijinsmash.riderz.ui.fragment.favorite;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.adapter.favorite.FavoriteRecyclerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.bartFavorites_error_tV) TextView mError;
    @BindView(R.id.bartFavorites_recyclerView) RecyclerView mRecyclerView;

    @Inject
    FavoritesViewModelFactory mFavoritesViewModelFactory;

    private FavoritesViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.view_favorites, container, false);
        ButterKnife.bind(this, inflatedView);
        return inflatedView;
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
        mViewModel.getFavorites().observe(this, data -> {
            if(data != null) {
                if(data.size() > 0) {
                    mError.setVisibility(View.GONE);
                    FavoriteRecyclerAdapter adapter = new FavoriteRecyclerAdapter(data);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });
    }
}
