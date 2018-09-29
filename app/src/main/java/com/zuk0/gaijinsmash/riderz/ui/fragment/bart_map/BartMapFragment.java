package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_map;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zuk0.gaijinsmash.riderz.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class BartMapFragment extends Fragment {

    @BindView(R.id.bartMap_imageView) ImageView mImageView;

    @Inject
    BartMapViewModelFactory mViewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.view_bart_map, container, false);
        ButterKnife.bind(this, mInflatedView);
        return mInflatedView;
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
        BartMapViewModel mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(BartMapViewModel.class);
        mViewModel.initBarMap(getActivity(), mImageView);
    }
}
