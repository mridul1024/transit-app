package com.example.gaijinsmash.transitapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;
import com.example.gaijinsmash.transitapp.view_adapter.TripViewAdapter;

import java.util.List;

public class BartResultsFragment extends Fragment {

    private ListView mListView;
    private Bundle mBundle;
    private List<FullTrip> mTripList;
    private ProgressBar mProgressBar;
    private View mInflatedView;

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
        mInflatedView = inflater.inflate(R.layout.results_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressBar = (ProgressBar) mInflatedView.findViewById(R.id.bart_results_progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView = (ListView) mInflatedView.findViewById(R.id.results_listView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBundle = getArguments();
        if(mBundle != null) {
            mTripList = mBundle.getParcelableArrayList("TripList");
        }
        if(mTripList != null) {
            TripViewAdapter adapter = new TripViewAdapter(mTripList, getActivity());
            mListView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        } else {
            Log.e("trip list", "null");
        }
    }
}
