package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.view_adapter.StationCustomViewAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Station;

import java.util.List;

public class ResultsFragment extends Fragment {

    private ListView listView;
    private static StationCustomViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.results_view, container, false);

        listView = (ListView) inflatedView.findViewById(R.id.results_listView);

        //new UpdateMainActivity(this).execute();

        return inflatedView;
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
