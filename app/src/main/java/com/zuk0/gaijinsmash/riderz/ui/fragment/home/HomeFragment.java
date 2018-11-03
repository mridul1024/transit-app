package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.databinding.ViewHomeBinding;
import com.zuk0.gaijinsmash.riderz.ui.adapter.bsa.BsaRecyclerAdapter;
import com.zuk0.gaijinsmash.riderz.ui.adapter.estimate.EstimateRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

// must use android.support.v4.app.Fragment for ViewModelProvider compatibility
public class HomeFragment extends Fragment  {

    @Inject
    HomeViewModelFactory mHomeViewModelFactory;

    private ViewHomeBinding mDataBinding;
    private HomeViewModel mViewModel;
    private static Bundle mListState;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_home, container, false);

        if(savedInstanceState != null) {
            //todo: add logic

        }

        return mDataBinding.getRoot();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listState = mDataBinding.homeEtdRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("RECYCLER_STATE", listState);
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if(state != null) {
            mListState = state.getParcelable("RECYCLER_STATE");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Parcelable listState = mDataBinding.homeEtdRecyclerView.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mListState != null) {
            mDataBinding.homeEtdRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        } else {
            attemptEstimateCall(mViewModel.doesPriorityExist());
        }
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel.class);
    }

    private void initPicture(HomeViewModel viewModel) {
        viewModel.initPic(Objects.requireNonNull(getActivity()), mViewModel.getHour(), mDataBinding.homeBannerImageView);
    }



    private void updateAdvisories(LiveData<BsaXmlResponse> bsa) {
        bsa.observe(this, bsaXmlResponse -> {
            if (bsaXmlResponse != null) {
                mDataBinding.bsaViewTimeTv.setText(mViewModel.initMessage(Objects.requireNonNull(getActivity()),mViewModel.is24HrTimeOn(getActivity()),bsaXmlResponse.getTime()));
                BsaRecyclerAdapter bsaAdapter = new BsaRecyclerAdapter(bsaXmlResponse.getBsaList());
                mDataBinding.homeBsaRecyclerView.setAdapter(bsaAdapter);
                mDataBinding.homeBsaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    private void attemptEstimateCall(boolean doesPriorityExist) {
        if(doesPriorityExist) {
            Favorite favorite = mViewModel.getFavorite();

            mViewModel.getTripLiveData(favorite.getOrigin(), favorite.getDestination()).observe(this, tripJsonResponse -> {
                List<Trip> trips = tripJsonResponse.getRoot().getSchedule().getRequest().getTripList();
                ArrayList<String> trainHeaders = new ArrayList<>();
                for(Trip trip: trips) {
                    String header = trip.getLegList().get(0).getTrainHeadStation();
                    if(!trainHeaders.contains(header)) {
                        trainHeaders.add(header); // add a unique train header
                        Log.i("HEADER", header);
                    }
                }
                favorite.setTrainHeaderStations(trainHeaders);

                loadEtd(favorite); // attempt etd list
            });
        }
    }

    private void loadEtd(Favorite favorite) {
        mViewModel.getEtdLiveData(favorite.getOrigin()).observe(this, etdXmlResponse -> {
            if(etdXmlResponse != null) {
                List<Etd> list = etdXmlResponse.getStation().getEtdList();
                List<Estimate> results = new ArrayList<>();
                if(list != null) {
                    for(Etd etd : list) {
                        if(favorite.getTrainHeaderStations().contains(etd.getDestinationAbbr())) {
                            Estimate estimate = etd.getEstimateList().get(0);
                            estimate.setOrigin(favorite.getOrigin()); // need to manually add this value
                            estimate.setDestination(etd.getDestination()); // need to manually add this value
                            results.add(estimate); //add the soonest departure
                        }
                    }
                    EstimateRecyclerAdapter etdAdapter = new EstimateRecyclerAdapter(results);
                    mDataBinding.homeEtdRecyclerView.setAdapter(etdAdapter);
                    mDataBinding.homeEtdRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });
    }

    private void updateProgressBar() {
        mDataBinding.homeEtdProgressBar.setVisibility(View.GONE);
    }
}
