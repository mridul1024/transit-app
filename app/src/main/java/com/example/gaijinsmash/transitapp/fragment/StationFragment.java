package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.view_adapter.StationViewAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class StationFragment extends Fragment {

    private ListView mListView;
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
        mInflatedView = inflater.inflate(R.layout.station_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = mInflatedView.findViewById(R.id.station_listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Because some station names are edited in the textView,
                // the address will ensure the right object
                // is found in the Database.
                String stationAddress = ((TextView) view.findViewById(R.id.stationAddress_textView)).getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("StationAddress", stationAddress);
                Fragment newFrag = new StationInfoFragment();
                newFrag.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContent, newFrag)
                        .addToBackStack(null).commit();
            }
        });
        mProgressBar = mInflatedView.findViewById(R.id.station_progress_bar);
        new GetStationsTask(this).execute();
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private static class GetStationsTask extends AsyncTask<Void, Void, List<Station>> {
        private WeakReference<StationFragment> mWeakRef;
        private List<Station> stationList = null;

        private GetStationsTask(StationFragment context) {
            mWeakRef = new WeakReference<>(context);
        }

        @Override
        protected List<Station> doInBackground(Void... voids) {
            StationFragment frag = mWeakRef.get();
            frag.mProgressBar.setVisibility(View.VISIBLE);
            StationDbHelper helper = new StationDbHelper(frag.getActivity());
            try {
                helper.initStationDb(frag.getActivity());
                if(stationList == null) {
                    stationList = helper.getDb().getStationDAO().getAllStations();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stationList;
        }

        protected void onPostExecute(List<Station> stations) {
            StationFragment frag = mWeakRef.get();
            if(stationList != null) {
                StationViewAdapter adapter = new StationViewAdapter(stationList, frag.getActivity());
                frag.mListView.setAdapter(adapter);
                frag.mProgressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.cannot_do_this), Toast.LENGTH_LONG).show();
                frag.mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
