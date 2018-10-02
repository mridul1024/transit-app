package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.zuk0.gaijinsmash.riderz.data.local.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartResultsViewModel extends AndroidViewModel {

    private LiveData<TripJsonResponse> mTrip;
    private TripRepository mTripRepository;
    private Favorite mFavorite;

    @Inject
    BartResultsViewModel(Application application, TripRepository tripRepository) {
        super(application);
        mTripRepository = tripRepository;
    }
    /****************************************************************
       Trips - origin and destination must be in abbreviated form
     ****************************************************************/
    LiveData<TripJsonResponse> getTrip(String origin, String destination, String date, String time) {
        if(mTrip == null) {
            mTrip = mTripRepository.getTrip(origin, destination, date, time);
        }
        return mTrip;
    }

    LiveData<List<Station>> getStationsFromDb(Context context, String origin, String destination) {
        return StationDatabase.getRoomDB(context).getStationDAO().getOriginAndDestination(origin, destination);
    }

    /****************************************************************
        Favorites
     ****************************************************************/
    Favorite createFavorite(String origin, String destination) {
        if(origin != null && destination != null) {
            mFavorite = new Favorite();

            Trip originTrip = new Trip();
            originTrip.setOrigin(origin);
            originTrip.setDestination(destination);
            Trip destTrip = new Trip();
            destTrip.setOrigin(destination);
            destTrip.setDestination(origin);
            mFavorite.setOriginTrip(originTrip);
            mFavorite.setDestinationTrip(destTrip);
            //todo: mFavoriteObject.setColors(BartRoutesUtils.getColorsSetFromLegList(legList));
        }
        return mFavorite;
    }

    public Favorite getFavorite() { return mFavorite; }

    void handleFavoritesIcon(RiderzEnums.FavoritesAction action, Favorite favorite) {
        new AddOrRemoveFavoriteTask(getApplication(), action, favorite).execute();
    }

    //check if favorite exists
    LiveData<Favorite> isTripFavorited(Trip trip1, Trip trip2) {
        return FavoriteDatabase.getRoomDB(getApplication()).getFavoriteDAO().findFavoriteByTrips(trip1, trip2);
    }

    private static class AddOrRemoveFavoriteTask extends AsyncTask<Void,Void,Void> {

        private WeakReference<Context> mWeakRef;
        private Favorite mFavorite;
        private RiderzEnums.FavoritesAction mAction;

        private AddOrRemoveFavoriteTask(Application context, RiderzEnums.FavoritesAction action, Favorite favorite) {
            mWeakRef = new WeakReference<>(context);
            this.mFavorite = favorite;
            this.mAction = action;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch(mAction){
                case ADD_FAVORITE:
                    if(FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().getPriorityCount() == 0) {
                        mFavorite.setPriority(Favorite.Priority.ON);
                    }
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().add(mFavorite);
                    break;
                case DELETE_FAVORITE:
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().delete(mFavorite);
                    break;
            }
            return null;
        }
    }
}
