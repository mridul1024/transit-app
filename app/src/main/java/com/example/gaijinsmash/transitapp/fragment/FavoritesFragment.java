package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.model.bart.Favorite;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private View mInflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // only create View here
        // Example:
        mInflatedView = inflater.inflate(R.layout.bart_favorites_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // All other UI elements are called here
        // Example:
        // mButton = (Button) mInflatedView.findViewById(R.id.help_report_button);
    }

    private class GetFavorites extends AsyncTask<Void, Void, List<Favorite>> {

        public GetFavorites() {

        }

        @Override
        protected List<Favorite> doInBackground(Void...voids) {

            return null;
        }

        @Override
        protected void onPostExecute(List<Favorite> list) {

        }

    }
}
