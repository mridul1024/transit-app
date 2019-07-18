package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.databinding.FragmentStationsBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class StationsFragment extends Fragment {

    @Inject
    StationsViewModelFactory mStationsViewModelFactory;

    private FragmentStationsBinding mDataBinding;
    private StationsViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stations, container, false);
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
        Bundle bundle = initBundle(view);
        launchStationInfoFragment(bundle);
    }

    private Bundle initBundle(View view) {
        String stationAbbr = ((TextView) view.findViewById(R.id.stationAbbr_textView)).getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("StationAbbr", stationAbbr); //todo: convert StationAbbr to enum
        return bundle;
    }

    private void launchStationInfoFragment(Bundle bundle) {
        NavHostFragment.findNavController(this).navigate(
                R.id.action_stationsFragment_to_stationInfoFragment,
                bundle,
                null,
                null
        );
    }

    private void initStationList() {
        mViewModel.getListFromDb(getActivity()).observe(this, stations -> {
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
