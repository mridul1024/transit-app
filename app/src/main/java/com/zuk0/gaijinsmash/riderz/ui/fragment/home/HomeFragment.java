package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.ui.adapter.bsa.BsaRecyclerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

// must use android.support.v4.app.Fragment for ViewModelProvider compatibility
public class HomeFragment extends Fragment  {

    @Inject
    HomeViewModelFactory mHomeViewModelFactory;

    private HomeViewModel mViewModel;

    @BindView(R.id.home_bsa_recyclerView) RecyclerView mAdvisoryRecyclerView;
    @BindView(R.id.home_etd_recyclerView) RecyclerView mEstimateRecyclerView;
    @BindView(R.id.home_etd_title) TextView mEtdTitle;
    @BindView(R.id.home_etd_error) TextView mEstimateErrorTv;
    @BindView(R.id.bsa_view_timeTv) TextView mBsaTimeTv;
    @BindView(R.id.home_etd_progressBar) ProgressBar mEtdProgressBar; // default is VISIBLE
    @BindView(R.id.home_banner_imageView) ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.view_home, container, false);
        ButterKnife.bind(this, mInflatedView);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initDagger();
        this.initViewModel();
        initPicture(mViewModel);
        updateAdvisories(mViewModel.getBsaLiveData());
        updateProgressBar();
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel.class);
    }

    private void initPicture(HomeViewModel viewModel) {
        viewModel.initPic(getActivity(), mViewModel.getHour(), mImageView);
    }

    private void updateAdvisories(LiveData<BsaXmlResponse> bsa) {
        bsa.observe(this, bsaXmlResponse -> {
            //update the ui here
            if (bsaXmlResponse != null) {
                mBsaTimeTv.setText(mViewModel.initMessage(getActivity(),mViewModel.is24HrTimeOn(getActivity()),bsaXmlResponse.getTime()));
                BsaRecyclerAdapter bsaAdapter = new BsaRecyclerAdapter(bsaXmlResponse.getBsaList());
                mAdvisoryRecyclerView.setAdapter(bsaAdapter);
                mAdvisoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    private void updateFavorites() {
        //todo: add observer here
    }

    private void updateProgressBar() {
        mEtdProgressBar.setVisibility(View.GONE);
    }
}
