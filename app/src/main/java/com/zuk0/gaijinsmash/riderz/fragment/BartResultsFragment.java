package com.zuk0.gaijinsmash.riderz.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
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
import com.zuk0.gaijinsmash.riderz.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.debug.MyDebug;
import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;
import com.zuk0.gaijinsmash.riderz.model.bart.FullTrip;
import com.zuk0.gaijinsmash.riderz.model.bart.Trip;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;
import com.zuk0.gaijinsmash.riderz.utils.BartRoutes;
import com.zuk0.gaijinsmash.riderz.xml_adapter.trip.TripViewAdapter;
import com.zuk0.gaijinsmash.riderz.xml_adapter.trip.TripXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BartResultsFragment extends Fragment {

    private ListView mListView;
    private List<FullTrip> mFullTripList;
    private ProgressBar mProgressBar;
    private View mInflatedView;
    private String mOrigin, mDestination;

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
        mInflatedView = inflater.inflate(R.layout.results_view, container, false);
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
            mFullTripList = mBundle.getParcelableArrayList("FullTripList");
            mOrigin = mBundle.getString("Origin");
            mDestination = mBundle.getString("Destination");
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

            HashSet<String> colors = new HashSet<>();
            for(FullTrip fullTrip : mFullTripList) {
                // get the first leg object
                String line = fullTrip.getLegList().get(0).getLine();
                colors.add(BartRoutes.lineToColor(line));
            }
            mFavoriteObject.setColors(colors);
        }

        // Checks database and displays appropriate view in toolbar
        new SetFavoritesTask(this).execute();
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
                new SetFavoritesTask(this, 1).execute();
                return true;
            case R.id.action_favorited:
                //todo: abstract dialog to a separate file.
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Remove Favorite");
                alertDialog.setMessage("Are you sure you want to remove this favorite?");
                alertDialog.setPositiveButton(getResources().getString(R.string.alert_dialog_yes), (dialog, which) -> new SetFavoritesTask(mBartResultsFragment, 2).execute());
                alertDialog.setNegativeButton(getResources().getString(R.string.alert_dialog_no), (dialog, which) -> dialog.cancel());
                alertDialog.show();
                return true;
        }
        return false;
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    private static class SetFavoritesTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<BartResultsFragment> mWeakRef;
        private int mStatus = 0;

        private SetFavoritesTask(BartResultsFragment context) {
            mWeakRef = new WeakReference<>(context);
        }

        private SetFavoritesTask(BartResultsFragment context, int status) {
            mWeakRef = new WeakReference<>(context);
            this.mStatus = status;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            BartResultsFragment frag = mWeakRef.get();
            FavoriteDatabase db = FavoriteDatabase.getRoomDB(frag.getActivity());
            switch(mStatus) {
                case 0:
                    return isCurrentTripFavorited(db, frag.mFavoriteObject);
                case 1:
                    // todo: check if favorite object is first two entries - abstract later
                    if (db.getFavoriteDAO().countFavorites() < 2) {
                        frag.mFavoriteObject.setPriority(Favorite.Priority.ON);
                    };

                    addToFavorites(db, frag.mFavoriteObject);

                    // This adds the inverse of the Trip
                    Favorite favoriteReversed = new Favorite();
                    favoriteReversed.setOrigin(frag.mDestination);
                    favoriteReversed.setDestination(frag.mOrigin);
                    if (db.getFavoriteDAO().countFavorites() < 2) {
                        favoriteReversed.setPriority(Favorite.Priority.ON);
                    };
                    String url = BartApiStringBuilder.getDetailedRoute(frag.mDestination, frag.mOrigin, "TODAY", "NOW");
                    List<FullTrip> tripList = new ArrayList<>();
                    try {
                        TripXMLParser parser = new TripXMLParser(frag.getActivity());
                        tripList = parser.getList(url);
                    } catch (IOException | XmlPullParserException e) {
                        Log.e("Exception", e.toString());
                    }

                    HashSet<String> routes = new HashSet<>();
                    for(FullTrip fullTrip : tripList) {
                        String line = fullTrip.getLegList().get(0).getLine();
                        routes.add(BartRoutes.lineToColor(line));
                    }
                    favoriteReversed.setColors(routes);

                    if(MyDebug.DEBUG)
                        Log.d("reversedFav", frag.mDestination +"/"+frag.mOrigin);
                    addToFavorites(db, favoriteReversed);
                    return true;
                case 2:
                    removeFromFavorites(db, frag.mFavoriteObject);
                    return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            BartResultsFragment frag = mWeakRef.get();
            if(result) {
                IS_FAVORITED_ON = true;
                frag.mFavoritedIcon.setVisible(true);
                frag.mFavoriteIcon.setVisible(false);
                if(mStatus == 1) {
                    Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                }
            } else {
                IS_FAVORITED_ON = false;
                frag.mFavoriteIcon.setVisible(true);
                frag.mFavoritedIcon.setVisible(false);
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------------------------------
    private static boolean isCurrentTripFavorited(FavoriteDatabase db, Favorite favorite) {
        if(db.getFavoriteDAO().getFavoritesByOrigin(favorite.getOrigin()) != null) {
            //if found, check what destination is
            List<Favorite> list = db.getFavoriteDAO().getFavoritesByOrigin(favorite.getOrigin());
            // search for matching favorite object
            for(Favorite x : list) {
                if(x.getDestination().equals(favorite.getDestination())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void addToFavorites(FavoriteDatabase db, Favorite favorite) {
        db.getFavoriteDAO().addStation(favorite);
    }

    private static void removeFromFavorites(FavoriteDatabase db, Favorite favorite) {
        db.getFavoriteDAO().delete(favorite);
    }
}
