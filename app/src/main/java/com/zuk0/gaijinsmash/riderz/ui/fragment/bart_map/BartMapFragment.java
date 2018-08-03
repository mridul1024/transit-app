package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zuk0.gaijinsmash.riderz.R;

import butterknife.BindView;
import dagger.android.support.AndroidSupportInjection;


public class BartMapFragment extends Fragment {

    @BindView(R.id.bartMap_imageView) ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_bart_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        this.initViewModel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void initViewModel() {
        BartMapViewModel mViewModel = ViewModelProviders.of(this).get(BartMapViewModel.class);
        mViewModel.initBarMap(getActivity(), mImageView);
    }
}
