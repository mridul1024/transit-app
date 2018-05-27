package com.zuk0.gaijinsmash.riderz.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.database.FavoriteDbHelper;
import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;
import com.zuk0.gaijinsmash.riderz.model.bart.FullTrip;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesHelper;
import com.zuk0.gaijinsmash.riderz.xml_adapter.trip.TripViewAdapter;
import com.zuk0.gaijinsmash.riderz.xml_adapter.trip.TripXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BartResultsFragment extends Fragment {

    private ListView mListView;
    private List<FullTrip> mFullTripList;
    private ProgressBar mProgressBar;
    private View mInflatedView;
    private String mOrigin, mDestination;
    private ArrayList<String> mTrainHeaders;

    private MenuItem mFavoriteIcon, mFavoritedIcon;
    private Favorite mFavoriteObject;

    private static final String BART = "BART";
    private static boolean IS_FAVORITED_ON = false;

    private BartResultsFragment mBartResultsFragment = this;
    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.view_results, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mProgressBar = mInflatedView.findViewById(R.id.bart_results_progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView = mInflatedView.findViewById(R.id.results_listView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        Bundle mBundle = getArguments();
        if(mBundle != null) {
            mFullTripList = mBundle.getParcelableArrayList(TripFragment.TripBundle.FULLTRIP_LIST.getValue());
            mOrigin = mBundle.getString(TripFragment.TripBundle.ORIGIN.getValue());
            mDestination = mBundle.getString(TripFragment.TripBundle.DESTINATION.getValue());
            mTrainHeaders = mBundle.getStringArrayList(TripFragment.TripBundle.TRAIN_HEADERS.getValue());
        }
        if(mFullTripList != null) {
            TripViewAdapter adapter = new TripViewAdapter(mFullTripList, getActivity(), mBartResultsFragment);
            mListView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }
        if(mOrigin != null && mDestination != null) {
            mFavoriteObject = new Favorite();

            //check db if first entry
            mFavoriteObject.setPriority(Favorite.Priority.ON);
            mFavoriteObject.setOrigin(mOrigin);
            mFavoriteObject.setDestination(mDestination);
            mFavoriteObject.setSystem(BART);
            mFavoriteObject.setColors(BartRoutesHelper.getColorsSetFromFullTrip(mFullTripList));
            mFavoriteObject.setTrainHeaderStations(mTrainHeaders);
        }

        // Checks database and displays appropriate view in toolbar
        new SetFavoritesTask(this, FavoritesTask.CHECK_CURRENT_TRIP).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite, menu);
        inflater.inflate(R.menu.favorited, menu);
        mFavoriteIcon = menu.findItem(R.id.action_favorite);
        mFavoritedIcon = menu.findItem(R.id.action_favorited);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_favorite:
                new SetFavoritesTask(this, FavoritesTask.ADD_FAVORITE).execute();
                return true;
            case R.id.action_favorited:
                initAlertDialog();
                return true;
        }
        return false;
    }

    public void initAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Remove Favorite");
        alertDialog.setMessage("Are you sure you want to remove this favorite?");
        alertDialog.setPositiveButton(getResources().getString(R.string.alert_dialog_yes), (dialog, which) -> new SetFavoritesTask(mBartResultsFragment, FavoritesTask.DELETE_FAVORITE).execute());
        alertDialog.setNegativeButton(getResources().getString(R.string.alert_dialog_no), (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private enum FavoritesTask {
        CHECK_CURRENT_TRIP, ADD_FAVORITE, DELETE_FAVORITE
    }

    private static class SetFavoritesTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<BartResultsFragment> mWeakRef;
        private FavoritesTask mTask;

        private SetFavoritesTask(BartResultsFragment context, FavoritesTask task) {
            mWeakRef = new WeakReference<>(context);
            mTask = task;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            BartResultsFragment frag = mWeakRef.get();
            FavoriteDbHelper db = new FavoriteDbHelper(frag.getActivity());
            switch(mTask) {
                case CHECK_CURRENT_TRIP:
                    return db.doesFavoriteExistAlready(frag.mFavoriteObject);
                case ADD_FAVORITE:
                    if (db.getFavoritesCount() < 2) {
                        frag.mFavoriteObject.setPriority(Favorite.Priority.ON);
                    }
                    db.addToFavorites(frag.mFavoriteObject);

                    // This adds the inverse of the Trip object
                    Favorite favoriteReversed = new Favorite();
                    favoriteReversed.setOrigin(frag.mDestination);
                    favoriteReversed.setDestination(frag.mOrigin);
                    if (db.getFavoritesCount()< 2) {
                        favoriteReversed.setPriority(Favorite.Priority.ON);
                    }
                    List<FullTrip> tripList = getFullTripList(frag.getActivity(), frag.mDestination, frag.mOrigin);
                    favoriteReversed.setColors(BartRoutesHelper.getColorsSetFromFullTrip(tripList));
                    favoriteReversed.setTrainHeaderStations(BartRoutesHelper.getTrainHeadersListFromFullTrip(tripList));
                    db.addToFavorites(favoriteReversed);
                    return true;
                case DELETE_FAVORITE:
                    db.removeFromFavorites(frag.mFavoriteObject);
                    return false;
            }
            db.closeDb();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            BartResultsFragment frag = mWeakRef.get();
            if(result) {
                IS_FAVORITED_ON = true;
                frag.mFavoritedIcon.setVisible(true);
                frag.mFavoriteIcon.setVisible(false);
                if(mTask == FavoritesTask.ADD_FAVORITE) {
                    Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                }
            } else {
                IS_FAVORITED_ON = false;
                frag.mFavoriteIcon.setVisible(true);
                frag.mFavoritedIcon.setVisible(false);
                if(mTask == FavoritesTask.DELETE_FAVORITE) {
                    Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.favorite_deleted), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------------------------------
    private static List<FullTrip> getFullTripList(Context context, String origin, String destination) {
        String url = BartApiStringBuilder.getDetailedRoute(origin, destination, "TODAY", "NOW");
        List<FullTrip> tripList = new ArrayList<>();
        try {
            TripXMLParser parser = new TripXMLParser(context);
            tripList = parser.getList(url);
        } catch (IOException | XmlPullParserException e) {
            Log.e("Exception", e.toString());
        }
        return tripList;
    }


}
