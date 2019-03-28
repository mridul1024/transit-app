package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums;
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.databinding.ViewResultsBinding;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment;
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.android.support.AndroidSupportInjection;

public class BartResultsFragment extends Fragment {

    @Inject
    BartResultsViewModelFactory mBartResultsViewModelFactory;

    private ViewResultsBinding mDataBinding;
    private BartResultsViewModel mViewModel;
    private String mOrigin, mDestination, mDate, mTime;
    private boolean mFromRecyclerAdapter = false;

    private MenuItem mFavoriteIcon, mFavoritedIcon;
    private Favorite mFavoriteObject;
    private List<Trip> mTripList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_results, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initBundleFromTripFragment();
        if(mFromRecyclerAdapter) {
            initTripCall(mOrigin, mDestination, mDate, mTime);
        } else {
            initStationsForTripCall(mOrigin, mDestination);
        }
        initFavoriteIcon(mOrigin, mDestination);
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite, menu);
        inflater.inflate(R.menu.favorited, menu);
        mFavoriteIcon = menu.findItem(R.id.action_favorite);
        mFavoritedIcon = menu.findItem(R.id.action_favorited);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_favorite:
                addFavorite(mFavoriteObject);
                return true;
            case R.id.action_favorited:
                removeFavorite();
                return true;
        }
        return false;
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mBartResultsViewModelFactory).get(BartResultsViewModel.class);
    }

    private void initBundleFromTripFragment() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            mOrigin = bundle.getString(TripFragment.TripBundle.ORIGIN.getValue());
            mDestination = bundle.getString(TripFragment.TripBundle.DESTINATION.getValue());
            mDate = bundle.getString(TripFragment.TripBundle.DATE.getValue());
            mTime = bundle.getString(TripFragment.TripBundle.TIME.getValue());
            mFromRecyclerAdapter = bundle.getBoolean("FAVORITE_RECYCLER_ADAPTER");
        }
    }

    private void initStationsForTripCall(String origin, String destination) {
        LiveData<List<Station>> liveData = mViewModel.getStationsFromDb(getActivity(), origin, destination);
        liveData.observe(this, data -> {
            String depart;
            String arrive;
            if (data != null && data.size() > 0) {
                if(data.get(0).getName().equals(origin)){
                    depart = data.get(0).getAbbr();
                    arrive = data.get(1).getAbbr();
                } else {
                    depart = data.get(1).getAbbr();
                    arrive = data.get(0).getAbbr();
                }
                initTripCall(depart, arrive, mDate, mTime);
            } else {
                Log.wtf("initLiveData for Trip", "NULL");
            }
        });
    }

    private void initTripCall(String originAbbr, String destAbbr, String date, String time) {
        mViewModel.getTrip(originAbbr, destAbbr, date, time)
            .observe(this, response -> {
                LiveDataWrapper.Status status = response.getStatus();
                if(status == LiveDataWrapper.Status.SUCCESS) {
                    mTripList = response.getData().getRoot().getSchedule().getRequest().getTripList();
                    initFavoriteObject(mOrigin, mDestination, mTripList);
                    initRecyclerView(mTripList);
                }

                if(status == LiveDataWrapper.Status.ERROR) {
                    Log.wtf("initTripCall", response.getMsg());
                }
        });
    }

    private void initRecyclerView(List<Trip> tripList) {
        if(tripList != null) {
            TripRecyclerAdapter adapter = new TripRecyclerAdapter(getActivity(), tripList);
            mDataBinding.resultsRecyclerView.setAdapter(adapter);
            mDataBinding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mDataBinding.bartResultsProgressBar.setVisibility(View.GONE);
        }
    }

    private void initFavoriteObject(String origin, String destination, List<Trip> tripList) {
        mFavoriteObject = mViewModel.createFavorite(origin, destination, tripList);
    }

    private void initFavoriteIcon(String origin, String destination) {
        String org;
        String dest;

        if(mFromRecyclerAdapter) {
            //convert to full name
            org = StationList.stationMap.get(origin.toUpperCase());
            dest = StationList.stationMap.get(destination.toUpperCase());
        } else {
            org = origin;
            dest = destination;
        }

        mViewModel.getFavoriteLiveData(org, dest).observe(this, data -> {
            if(data != null) {
                // Current trip is already a Favorite
                mFavoritedIcon.setVisible(true);
                mFavoriteIcon.setVisible(false);
            } else {
                mFavoritedIcon.setVisible(false);
                mFavoriteIcon.setVisible(true);
            }
        });
    }

    private void addFavorite(Favorite favorite) {
        mViewModel.handleFavoritesIcon(RiderzEnums.FavoritesAction.ADD_FAVORITE, favorite);
        Toast.makeText(getActivity(), getResources().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getResources().getString(R.string.alert_dialog_remove_favorite_title));
        alertDialog.setMessage(getResources().getString(R.string.alert_dialog_confirmation));
        alertDialog.setPositiveButton(getResources().getString(R.string.alert_dialog_yes),
                (dialog, which) -> {
                    mViewModel.handleFavoritesIcon(RiderzEnums.FavoritesAction.DELETE_FAVORITE, mFavoriteObject);
                    mFavoritedIcon.setVisible(false);
                    mFavoriteIcon.setVisible(true);
                });
        alertDialog.setNegativeButton(getResources().getString(R.string.alert_dialog_no), (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
