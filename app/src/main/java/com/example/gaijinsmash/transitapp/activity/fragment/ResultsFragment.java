package com.example.gaijinsmash.transitapp.activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.adapter.StationCustomAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Station;

import java.util.List;

/**
 * Created by ryanj on 7/31/2017.
 */

public class ResultsFragment extends Fragment {

    ListView listView;
    private static StationCustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mInflatedView = inflater.inflate(R.layout.results_view, container, false);

        listView = (ListView) mInflatedView.findViewById(R.id.results_listView);

        //new UpdateMainActivity(this).execute();

        return mInflatedView;
    }

    private class UpdateMainActivity extends AsyncTask<Void, Void, List<Station>> {
        private Context context;

        public UpdateMainActivity(Context context) {
            this.context = context;
        }

        @Override
        protected List<Station> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(List<Station> stationList) {

        }

    }
}
