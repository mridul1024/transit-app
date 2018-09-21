package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.local.helper.FavoriteDbHelper;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartResultsViewModel extends ViewModel {

    private LiveData<TripJsonResponse> mTrip;
    private TripRepository mTripRepository;

    @Inject
    BartResultsViewModel(TripRepository tripRepository) {
        mTripRepository = tripRepository;
    }

    /*
       origin and destination must be in abbreviated form
    */
    public LiveData<TripJsonResponse> getTrip(String origin, String destination, String date, String time) {
        if(mTrip == null) {
            mTrip = mTripRepository.getTrip(origin, destination, date, time);
        }
        return mTrip;
    }

    public LiveData<List<Station>> getStationsFromDb(Context context, String origin, String destination) {
        return StationDatabase.getRoomDB(context).getStationDAO().getOriginAndDestination(origin, destination);
    }

    public void initFavoritesIcon() {

    }

    private void loadData() {
    }


    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------
    /*
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
                    //List<FullTrip> tripList = getFullTripList(frag.getActivity(), frag.mDestination, frag.mOrigin);
                    //favoriteReversed.setColors(BartRoutesUtils.getColorsSetFromFullTrip(tripList));
                    //favoriteReversed.setTrainHeaderStations(BartRoutesUtils.getTrainHeadersListFromFullTrip(tripList));
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
    */
}
