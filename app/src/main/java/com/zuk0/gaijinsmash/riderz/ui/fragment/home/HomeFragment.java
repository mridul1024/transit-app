package com.zuk0.gaijinsmash.riderz.ui.fragment.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.databinding.ViewHomeBinding;
import com.zuk0.gaijinsmash.riderz.ui.adapter.bsa.BsaRecyclerAdapter;
import com.zuk0.gaijinsmash.riderz.ui.adapter.estimate.EstimateRecyclerAdapter;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;

//todo: implement caching and time management of ETDs
public class HomeFragment extends Fragment {

    @Inject
    HomeViewModelFactory mHomeViewModelFactory;

    private ViewHomeBinding mDataBinding;
    private HomeViewModel mViewModel;
    private Favorite mFavorite;

    private List<Estimate> mInverseEstimateList;
    private List<Estimate> mFavoriteEstimateList;

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

        AppBarLayout appBarLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.main_app_bar_layout);
        appBarLayout.setExpanded(true);

        if(mViewModel.doesPriorityExist()) {
            initFavorite(mViewModel);
            loadTripData(mFavorite);
            loadFavoriteEtd(mFavorite);
        } else {
            updateProgressBar();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
    }

    @Override
    public void onPause() {
        super.onPause();

        //todo: destroy timers

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mHomeViewModelFactory).get(HomeViewModel.class);
    }

    private void initPicture(HomeViewModel viewModel) {
        ImageView imageView = Objects.requireNonNull(getActivity()).findViewById(R.id.home_banner_imageView);
        viewModel.initPic(Objects.requireNonNull(getActivity()), mViewModel.getHour(), imageView);
    }

    /*
        Fetches data for BART advisories - i.e. delay reports
     */
    private void loadAdvisories(LiveData<BsaXmlResponse> bsa) {
        bsa.observe(this, bsaXmlResponse -> {
            if (bsaXmlResponse != null) {
                mDataBinding.bsaViewTimeTv.setText(mViewModel.initMessage(Objects.requireNonNull(getActivity()),mViewModel.is24HrTimeOn(getActivity()),bsaXmlResponse.getTime()));
                mViewModel.setBsaList(bsaXmlResponse.getBsaList());

                BsaRecyclerAdapter bsaAdapter = new BsaRecyclerAdapter(bsaXmlResponse.getBsaList());
                mDataBinding.homeBsaRecyclerView.setAdapter(bsaAdapter);
                mDataBinding.homeBsaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    private void initFavorite(HomeViewModel viewModel) {
        mFavorite = viewModel.getFavorite();
    }

    /*
       Fetches an ETD list for the user's favorite.priority route
   */
    private void loadFavoriteEtd(Favorite favorite) {
        mViewModel.getEtdLiveData(favorite.getOrigin()).observe(this, data -> {
            if(data != null && data.getStation().getEtdList() != null) {
                mFavoriteEstimateList = mViewModel.getEstimatesFromEtd(favorite, data.getStation().getEtdList());
                EstimateRecyclerAdapter etdAdapter = new EstimateRecyclerAdapter(mFavoriteEstimateList);
                mDataBinding.homeEtdRecyclerView.setAdapter(etdAdapter);
                mDataBinding.homeEtdRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    private void loadTripData(Favorite favorite) {
        // Create a favorite object to handle the return trip
        mViewModel.getTripLiveData(favorite.getDestination(), favorite.getOrigin()).observe(this, tripJsonResponse -> {
            List<Trip> trips;
            if (tripJsonResponse != null) {
                trips = tripJsonResponse.getRoot().getSchedule().getRequest().getTripList();
                Favorite inverse = mViewModel.createFavoriteInverse(trips, favorite);
                loadInverseEtd(inverse);
            }
        });
    }

    /*
        Fetches an ETD for the opposite direction of the favorite
     */
    private void loadInverseEtd(Favorite inverse) {
        mViewModel.getEtdLiveData(inverse.getOrigin()).observe(this, data -> {
            if(data != null && data.getStation().getEtdList() != null) {
                mInverseEstimateList = mViewModel.getEstimatesFromEtd(inverse, data.getStation().getEtdList());
                EstimateRecyclerAdapter etdAdapter = new EstimateRecyclerAdapter(mInverseEstimateList);
                mDataBinding.homeEtdRecyclerView2.setAdapter(etdAdapter);
                mDataBinding.homeEtdRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            updateProgressBar();
        });
    }

    private void updateProgressBar() {
        mDataBinding.homeEtdProgressBar.setVisibility(View.GONE);
    }
}
