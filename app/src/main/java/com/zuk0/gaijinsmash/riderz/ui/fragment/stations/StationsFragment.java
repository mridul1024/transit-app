package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.adapter.station.StationRecyclerAdapter;
import com.zuk0.gaijinsmash.riderz.ui.fragment.station_info.StationInfoFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class StationsFragment extends Fragment {

    @Inject
    StationsViewModelFactory mStationsViewModelFactory;

    private StationsViewModel mViewModel;

    @BindView(R.id.station_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.station_progress_bar) ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mInflatedView = inflater.inflate(R.layout.view_station, container, false);
        ButterKnife.bind(this, mInflatedView);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDagger();
        initViewModel();
        initStationList();
    }

    private void initDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mStationsViewModelFactory).get(StationsViewModel.class);
    }

    private void handleListItemClick(View view) {
        //todo: probably can delete stationAddress - not needed
        Bundle bundle = initBundle(view);
        loadNewFragment(bundle);
    }

    private Bundle initBundle(View view) {
        String stationAddress = ((TextView) view.findViewById(R.id.stationAddress_textView)).getText().toString();
        String stationAbbr = ((TextView) view.findViewById(R.id.stationAbbr_textView)).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("StationAddress", stationAddress); //todo: convert StationAddress to enum
        bundle.putString("StationAbbr", stationAbbr); //todo: convert StationAbbr to enum
        return bundle;
    }

    // todo: abstract this
    private void loadNewFragment(Bundle bundle) {
        Fragment newFrag = new StationInfoFragment();
        newFrag.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction;
        if (manager != null) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.fragmentContent, newFrag)
                    .addToBackStack(null).commit();
        }
    }

    private void initStationList() {
        mViewModel.getListFromDb(getActivity())
        .observe(this, stations -> {
            //update the ui
            if(stations != null) {
                StationRecyclerAdapter adapter = new StationRecyclerAdapter(stations);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.setClickListener(this::handleListItemClick);
            } else {
                mViewModel.getListFromRepo()
                        .observe(this, data -> {
                            StationRecyclerAdapter adapter;
                            if (data != null) {
                                adapter = new StationRecyclerAdapter(data.getStationList());
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter.setClickListener(this::handleListItemClick);
                            } else {
                                Log.wtf("StationsFragment", "error with list");
                            }
                        });
            }
            mProgressBar.setVisibility(View.GONE);
        });
    }
}
