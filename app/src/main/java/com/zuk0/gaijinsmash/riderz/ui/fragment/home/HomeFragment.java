package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.zuk0.gaijinsmash.riderz.data.local.entity.MergedTrip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
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
    private Favorite mFavorite;

    private static List<Bsa> mBsaList;
    private static Parcelable mListState;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_home, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initDagger();
        this.initViewModel();
        initPicture(mViewModel);
        loadAdvisories(mViewModel.getBsaLiveData());

        if(mViewModel.doesPriorityExist()) {
            initFavorite(mViewModel);
            loadEtdData(mFavorite);
        }

        updateProgressBar();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mDataBinding.homeEtdRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("RECYCLER_STATE", mListState);
        // todo: save BSA State
        // todo: save ETD state
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        //mListState = state.getParcelable("RECYCLER_STATE");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mDataBinding.homeEtdRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
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

    private void loadAdvisories(LiveData<BsaXmlResponse> bsa) {
        bsa.observe(this, bsaXmlResponse -> {
            if (bsaXmlResponse != null) {
                mDataBinding.bsaViewTimeTv.setText(mViewModel.initMessage(Objects.requireNonNull(getActivity()),mViewModel.is24HrTimeOn(getActivity()),bsaXmlResponse.getTime()));
                mBsaList = bsaXmlResponse.getBsaList();
                BsaRecyclerAdapter bsaAdapter = new BsaRecyclerAdapter(mBsaList);
                mDataBinding.homeBsaRecyclerView.setAdapter(bsaAdapter);
                mDataBinding.homeBsaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    private void initFavorite(HomeViewModel viewModel) {
        mFavorite = viewModel.getFavorite();
    }

    private void loadEtdData(Favorite favorite) {
        // Create a favorite object to handle the return trip
        mViewModel.getTripLiveData(favorite.getDestination(), favorite.getOrigin()).observe(this, tripJsonResponse -> {
            List<Trip> trips = tripJsonResponse.getRoot().getSchedule().getRequest().getTripList();
            Favorite inverse = mViewModel.createFavoriteInverse(trips, favorite);

            observeMediatorData(mergeEtdData(mFavorite, inverse));
        });
    }

    private MediatorLiveData<List<Estimate>> mergeEtdData(Favorite favorite, Favorite inverse) {
        LiveData<EtdXmlResponse> etd1 = mViewModel.getEtdLiveData(favorite.getOrigin());
        LiveData<EtdXmlResponse> etd2 = mViewModel.getEtdLiveData(inverse.getOrigin());

        MediatorLiveData<List<Estimate>> mediatorLiveData = new MediatorLiveData();

        if(etd1 != null) {
            mediatorLiveData.addSource(etd1, value -> {
                List<Etd> list = value.getStation().getEtdList();
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
                }
                mediatorLiveData.setValue(results);
            });
        }

        if(etd2 != null) {
            mediatorLiveData.addSource(etd2, value -> {
                List<Etd> list = value.getStation().getEtdList();
                List<Estimate> results = new ArrayList<>();
                if(list != null) {
                    for(Etd etd : list) {
                        if(inverse.getTrainHeaderStations().contains(etd.getDestinationAbbr())) {
                            Estimate estimate = etd.getEstimateList().get(0);
                            estimate.setOrigin(inverse.getOrigin());
                            estimate.setDestination(etd.getDestination());
                            results.add(estimate);
                        }
                    }
                }
                mediatorLiveData.setValue(results);
            });
        }

        return mediatorLiveData;
    }

    private void observeMediatorData(MediatorLiveData<List<Estimate>> data) {
        if(data !=  null) {
            data.observe(this, estimateList -> {
                Log.i("ESTIMATE LIST", estimateList.toString());
                EstimateRecyclerAdapter etdAdapter = new EstimateRecyclerAdapter(estimateList);
                mDataBinding.homeEtdRecyclerView.setAdapter(etdAdapter);
                mDataBinding.homeEtdRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            });
        }
    }

    private void updateProgressBar() {
        mDataBinding.homeEtdProgressBar.setVisibility(View.GONE);
    }
}
