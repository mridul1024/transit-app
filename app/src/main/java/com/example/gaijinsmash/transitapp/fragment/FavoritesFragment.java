package com.example.gaijinsmash.transitapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.FavoriteDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Favorite;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private View mInflatedView;
    private TextView mError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.bart_favorites_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mError = (TextView) mInflatedView.findViewById(R.id.bartFavorites_error_tV);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetFavorites(getActivity()).execute();
    }

    private class GetFavorites extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;
        private List<Favorite> mFavoritesList;

        public GetFavorites(Context context) {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            // connect to DB
            FavoriteDatabase db = FavoriteDatabase.getRoomDB(mContext);
            mFavoritesList = db.getFavoriteDAO().getAllFavorites();
            if(mFavoritesList != null) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                // set view

            } else {
                // set a default view for empty favorites list
                mError.setText(getResources().getString(R.string.bart_favorites_empty));
            }
        }

    }
}
