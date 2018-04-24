package com.zuk0.gaijinsmash.riderz.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;
import com.zuk0.gaijinsmash.riderz.view_adapter.FavoriteViewAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private View mInflatedView;
    private TextView mError;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.bart_favorites_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mError = mInflatedView.findViewById(R.id.bartFavorites_error_tV);
        mListView = mInflatedView.findViewById(R.id.bartFavorites_listView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        new GetFavoritesTask(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static class GetFavoritesTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<FavoritesFragment> mWeakRef;
        private List<Favorite> mFavoritesList;

        private GetFavoritesTask(FavoritesFragment context) {
            mWeakRef = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            FavoritesFragment frag = mWeakRef.get();
            // connect to DB
            FavoriteDatabase db = FavoriteDatabase.getRoomDB(frag.getActivity());
            int numberOfFavorites = db.getFavoriteDAO().countFavorites();
            if(numberOfFavorites > 0) {
                mFavoritesList = db.getFavoriteDAO().getAllFavorites();
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            FavoritesFragment frag = mWeakRef.get();
            if(result) {
                // set view
                if(mFavoritesList != null) {
                    FavoriteViewAdapter adapter = new FavoriteViewAdapter(mFavoritesList, frag.getActivity(), frag);
                    frag.mListView.setAdapter(adapter);
                } else {
                    frag.mError.setText(frag.getResources().getString(R.string.bart_favorites_empty));
                }
            } else {
                // set a default view for empty favorites list
                if(frag.isAdded())
                    frag.mError.setText(frag.getResources().getString(R.string.bart_favorites_empty));
            }
        }
    }
}
