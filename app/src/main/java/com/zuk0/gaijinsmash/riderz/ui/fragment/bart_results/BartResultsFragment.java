package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Leg;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.ui.adapter.trip.TripRecyclerAdapter;
import com.zuk0.gaijinsmash.riderz.ui.fragment.trip.TripFragment;
import com.zuk0.gaijinsmash.riderz.utils.SnackbarUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class BartResultsFragment extends Fragment {

    @BindView(R.id.results_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.bart_results_progressBar) ProgressBar mProgressBar;

    @Inject
    BartResultsViewModelFactory mBartResultsViewModelFactory;

    private BartResultsViewModel mViewModel;
    private String mOrigin, mDestination, mDate, mTime;

    private MenuItem mFavoriteIcon, mFavoritedIcon;
    private Favorite mFavoriteObject;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.view_results, container, false);
        ButterKnife.bind(this, inflatedView);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initBundleFromTripFragment();
        initStationsForTripCall(mOrigin, mDestination);
        initFavoriteObject(mOrigin, mDestination);
        initFavoriteIcon(mFavoriteObject);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite, menu);
        inflater.inflate(R.menu.favorited, menu);
        mFavoriteIcon = menu.findItem(R.id.action_favorite);
        mFavoritedIcon = menu.findItem(R.id.action_favorited);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_favorite:
                addFavorite(mFavoriteObject);
                return true;
            case R.id.action_favorited:
                removeFavorite(mFavoriteObject);
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
                if(response != null) {
                    List<Trip> list = response.getRoot().getSchedule().getRequest().getTripList();
                    initRecylerView(list);
                } else {
                    Log.wtf("initTripCall", "error");
                }
        });
    }

    private void initRecylerView(List<Trip> tripList) {
        if(tripList != null) {
            TripRecyclerAdapter adapter = new TripRecyclerAdapter(getActivity(), tripList);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initFavoriteObject(String origin, String destination) {
        mFavoriteObject = mViewModel.createFavorite(origin, destination);
    }

    private void initFavoriteIcon(Favorite favorite) {
        mViewModel.isTripFavorited(favorite.getOriginTrip(), favorite.getDestinationTrip()).observe(this, data -> {
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
        mViewModel.handleFavoritesIcon(BartResultsViewModel.FavoritesAction.ADD_FAVORITE, favorite);
        Toast.makeText(getActivity(), getResources()
                .getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(Favorite favorite) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getResources().getString(R.string.alert_dialog_remove_favorite_title));
        alertDialog.setMessage(getResources().getString(R.string.alert_dialog_confirmation));
        alertDialog.setPositiveButton(getResources().getString(R.string.alert_dialog_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.handleFavoritesIcon(BartResultsViewModel.FavoritesAction.DELETE_FAVORITE, favorite);
                        mFavoritedIcon.setVisible(false);
                        mFavoriteIcon.setVisible(true);
                    }
                });
        alertDialog.setNegativeButton(getResources().getString(R.string.alert_dialog_no), (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
