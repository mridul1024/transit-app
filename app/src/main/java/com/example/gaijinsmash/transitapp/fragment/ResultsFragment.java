package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Trip;
import com.example.gaijinsmash.transitapp.view_adapter.StationViewAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.view_adapter.TripViewAdapter;

import java.util.List;

public class ResultsFragment extends Fragment {

    private ListView mListView;
    private List<Trip> mTripList;
    private Bundle mBundle;

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Initialize data here
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.results_view, container, false);
        mListView = (ListView) inflatedView.findViewById(R.id.results_listView);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBundle = getArguments();
        if(mBundle != null) {
            mTripList = mBundle.getParcelableArrayList("Trips");
        }
        if(mTripList != null) {
            TripViewAdapter adapter = new TripViewAdapter(mTripList, getActivity());
            mListView.setAdapter(adapter);
        } else {
            Log.e("trip list", "null");
        }
    }
}
