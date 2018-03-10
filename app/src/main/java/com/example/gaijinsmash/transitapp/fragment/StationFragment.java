package com.example.gaijinsmash.transitapp.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.database.StationDbHelper;
import com.example.gaijinsmash.transitapp.view_adapter.StationViewAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Station;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class StationFragment extends Fragment {

    private ListView mListView;
    private ProgressBar mProgressBar;

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
        View inflatedView = inflater.inflate(R.layout.station_view, container, false);
        mListView = inflatedView.findViewById(R.id.station_listView);
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
        mProgressBar = (ProgressBar) inflatedView.findViewById(R.id.station_progress_bar);
        new GetStationsTask(getActivity()).execute();

        return inflatedView;
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private class GetStationsTask extends AsyncTask<Void, Void, List<Station>> {

        private List<Station> stationList = null;
        private Context mContext;

        public GetStationsTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Station> doInBackground(Void... voids) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                StationDbHelper.initStationDb(mContext);
                if(stationList == null) {
                    StationDatabase db = StationDatabase.getRoomDB(mContext);
                    stationList = db.getStationDAO().getAllStations();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stationList;
        }

        protected void onPostExecute(List<Station> stations) {
            if(stationList != null) {
                StationViewAdapter adapter = new StationViewAdapter(stationList, mContext);
                mListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(mContext, getString(R.string.cannot_do_this), Toast.LENGTH_LONG);
                Log.e("onPostExecute()", "stationList is NULL");
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
