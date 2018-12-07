package com.zuk0.gaijinsmash.riderz.ui.fragment.stations;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.databinding.ViewStationBinding;
import com.zuk0.gaijinsmash.riderz.ui.adapter.station.StationRecyclerAdapter;
import com.zuk0.gaijinsmash.riderz.ui.fragment.station_info.StationInfoFragment;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.android.support.AndroidSupportInjection;

public class StationsFragment extends Fragment {

    @Inject
    StationsViewModelFactory mStationsViewModelFactory;

    private ViewStationBinding mDataBinding;
    private StationsViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.view_station, container, false);
        return mDataBinding.getRoot();
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
                mDataBinding.stationRecyclerView.setAdapter(adapter);
                mDataBinding.stationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.setClickListener(this::handleListItemClick);
            } else {
                mViewModel.getListFromRepo()
                        .observe(this, data -> {
                            StationRecyclerAdapter adapter;
                            if (data != null) {
                                adapter = new StationRecyclerAdapter(data.getStationList());
                                mDataBinding.stationRecyclerView.setAdapter(adapter);
                                mDataBinding.stationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter.setClickListener(this::handleListItemClick);
                            } else {
                                Log.wtf("StationsFragment", "error with list");
                            }
                        });
            }
            mDataBinding.stationProgressBar.setVisibility(View.GONE);
        });
    }
}
