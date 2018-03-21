package com.example.gaijinsmash.transitapp.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.FavoriteDatabase;
import com.example.gaijinsmash.transitapp.dialog.FavoriteDialog;
import com.example.gaijinsmash.transitapp.model.bart.Favorite;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;
import com.example.gaijinsmash.transitapp.model.bart.Trip;
import com.example.gaijinsmash.transitapp.view_adapter.TripViewAdapter;

import java.util.List;

public class BartResultsFragment extends Fragment {

    private ListView mListView;
    private Bundle mBundle;
    private List<FullTrip> mTripList;
    private ProgressBar mProgressBar;
    private View mInflatedView;
    private String[] mFavoriteArray;
    private Toolbar mToolbar;
    private Favorite mFavorite;
    private static final String BART = "BART";

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
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
            mFavoriteArray = mBundle.getStringArray("Favorite");
        }
        if(mTripList != null) {
            TripViewAdapter adapter = new TripViewAdapter(mTripList, getActivity());
            mListView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }
        if(mFavoriteArray != null) {
            mFavorite.setOrigin(mFavoriteArray[0]);
            mFavorite.setDestination(mFavoriteArray[1]);
            mFavorite.setSystem(BART);
        }

        // Checks database and displays appropriate view in toolbar
        new GetFavorites(getActivity()).execute();

        // event listener on user click
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                // if user is creating a favorite
                if(id == R.id.action_favorite) {
                    new SetFavorite(getActivity(), mFavorite);
                    return true;
                }

                // if user is destroying favorite
                if(id == R.id.action_favorited) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Remove Favorite");
                    alertDialog.setMessage("Are you sure you want to remove this favorite?");
                    alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //todo: remove favorite action
                            // delete from db and change icon view

                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    return true;
                }

                return false;
            }
        });
    }

    /*
        This AsyncTask checks if the current trip has already been saved to Favorites
    */
    private class GetFavorites extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;

        public GetFavorites(Context context) {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String origin = mFavorite.getOrigin();
            String destination = mFavorite.getDestination();

            // check if object exists in DB
            // todo: does db need to be initialized?
            FavoriteDatabase db = FavoriteDatabase.getRoomDB(mContext);
            if(db.getFavoriteDAO().getFavoritesByOrigin(origin) != null) {
                //if found, check what destination is
                List<Favorite> list = db.getFavoriteDAO().getFavoritesByOrigin(origin);
                for(Favorite x : list) {
                    if(x.getDestination().equals(destination)) {
                        return true;
                    }
                }

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                // if Favorite is found in DB , change favorite icon in toolbar
                mToolbar.inflateMenu(R.menu.favorited);
            } else {
                mToolbar.inflateMenu(R.menu.favorite);
            }
        }
    }

    private class SetFavorite extends AsyncTask<Void, Void, Boolean> {
        private Favorite mFavorite;
        private Context mContext;

        public SetFavorite(Context context, Favorite favorite) {
            this.mContext = context;
            this.mFavorite = favorite;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            FavoriteDatabase db = FavoriteDatabase.getRoomDB(mContext);
            db.getFavoriteDAO().addStation(mFavorite);
            //todo: need to add checks
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(mContext, "Added to favorites!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
