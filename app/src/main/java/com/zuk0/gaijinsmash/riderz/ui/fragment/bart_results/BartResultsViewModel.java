package com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
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
            mFavorite.setOrigin(origin);
            mFavorite.setDestination(destination);
            //todo: mFavoriteObject.setColors(BartRoutesUtils.getColorsSetFromLegList(legList));
        }
        return mFavorite;
    }

    public Favorite getFavorite() { return mFavorite; }

    void handleFavoritesIcon(RiderzEnums.FavoritesAction action, Favorite favorite) {
        new AddOrRemoveFavoriteTask(getApplication(), action, favorite).execute();
    }

    //check if favorite exists
    LiveData<Favorite> isTripFavorited(Favorite favorite) {
        return FavoriteDatabase.getRoomDB(getApplication()).getFavoriteDAO().getLiveDataFavorite(favorite.getOrigin(), favorite.getDestination());
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
                    } else {
                        mFavorite.setPriority(Favorite.Priority.OFF);
                    }
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().save(mFavorite);
                    break;
                case DELETE_FAVORITE:
                    FavoriteDao dao = FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO();
                    Favorite one = dao.getFavorite(mFavorite.getOrigin(), mFavorite.getDestination());
                    Favorite two = dao.getFavorite(mFavorite.getDestination(), mFavorite.getOrigin());
                    if(one != null) {
                        dao.delete(one);
                    }
                    if(two != null) {
                        dao.delete(two);
                    }
                    break;
            }
            return null;
        }
    }
}
