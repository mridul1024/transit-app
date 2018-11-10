package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.databinding.ViewBartMapBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class BartMapFragment extends Fragment {

    private ViewBartMapBinding mDataBinding;

    @Inject
    BartMapViewModelFactory mViewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_bart_map, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        this.initViewModel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViewModel() {
        BartMapViewModel mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(BartMapViewModel.class);
        mViewModel.initBartMap(getActivity(), mDataBinding.bartMapImageView);
    }
}
