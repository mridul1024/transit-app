package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.ui.adapter.station.StationViewAdapter;
import com.zuk0.gaijinsmash.riderz.ui.fragment.station_info.StationInfoFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class StationsFragment extends Fragment {

    @Inject
    StationsViewModelFactory mStationsViewModelFactory;

    private StationsViewModel mViewModel;

    @BindView(R.id.station_listView) ListView mListView;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mListView.setOnItemClickListener((parent, view1, position, id) -> handleListItemClick(view1));
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

    private void handleListItemClick(View view1) {
        //todo: probably can delete stationAddress - not needed
        String stationAddress = ((TextView) view1.findViewById(R.id.stationAddress_textView)).getText().toString();
        String stationAbbr = ((TextView) view1.findViewById(R.id.stationAbbr_textView)).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("StationAddress", stationAddress); //todo: convert StationAddress to enum
        bundle.putString("StationAbbr", stationAbbr); //todo: convert StationAbbr to enum
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
                StationViewAdapter adapter = new StationViewAdapter(stations, getActivity());
                mListView.setAdapter(adapter);
            } else {
                mViewModel.getListFromRepo()
                        .observe(this, data -> {
                            StationViewAdapter adapter = null;
                            if (data != null) {
                                adapter = new StationViewAdapter(data.getStationList(), getActivity());
                                mListView.setAdapter(adapter);
                            } else {
                                Log.wtf("StationsFragment", "error with list");
                            }
                        });
            }

            mProgressBar.setVisibility(View.GONE);
        });
    }
}
