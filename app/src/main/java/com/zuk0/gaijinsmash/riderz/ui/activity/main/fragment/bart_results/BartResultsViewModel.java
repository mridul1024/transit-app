package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.TripJsonResponse;
import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums;
import com.zuk0.gaijinsmash.riderz.data.remote.repository.TripRepository;
import com.zuk0.gaijinsmash.riderz.ui.shared.livedata.LiveDataWrapper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BartResultsViewModel extends AndroidViewModel {

    private LiveData<LiveDataWrapper<TripJsonResponse>> mTrip;
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
    LiveData<LiveDataWrapper<TripJsonResponse>> getTrip(String origin, String destination, String date, String time) {
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
    Favorite createFavorite(String origin, String destination, List<Trip> tripList) {
        if(origin != null && destination != null) {
            mFavorite = new Favorite();
            mFavorite.setOrigin(origin);
            mFavorite.setDestination(destination);
            ArrayList<String> trainHeaders = new ArrayList<>();
            for(Trip trip: tripList) {
                String header = trip.getLegList().get(0).getTrainHeadStation();
                if(!trainHeaders.contains(header)) {
                    trainHeaders.add(header); // add a unique train header
                    Log.i("HEADER", header);
                }
            }
            mFavorite.setTrainHeaderStations(trainHeaders);
        }
        return mFavorite;
    }

    public Favorite getFavorite() { return mFavorite; }

    void handleFavoritesIcon(RiderzEnums.FavoritesAction action, Favorite favorite) {
        new AddOrRemoveFavoriteTask(getApplication(), action, favorite).execute();
    }

    LiveData<Favorite> getFavoriteLiveData(String origin, String destination) {
        return FavoriteDatabase.getRoomDB(getApplication()).getFavoriteDAO().getLiveDataFavorite(origin, destination);
    }


    //todo : use rx java instead
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
