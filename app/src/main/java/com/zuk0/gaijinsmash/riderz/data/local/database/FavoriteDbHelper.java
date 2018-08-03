package com.zuk0.gaijinsmash.riderz.data.local.database;

import android.content.Context;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;

import java.util.List;

public class FavoriteDbHelper {

    private FavoriteDatabase mDatabase;

    public FavoriteDbHelper(Context context) {
        mDatabase = FavoriteDatabase.getRoomDB(context);
    }

    public void closeDb() {
        mDatabase.close();
    }

    public int getFavoritesCount() {
        int count = mDatabase.getFavoriteDAO().countFavorites();
        Log.i("fav count", String.valueOf(count));
        return count;
    }

    public  List<Favorite> getFavoritesByPriority() {
        return mDatabase.getFavoriteDAO().getAllFavoritesByPriority(Favorite.Priority.ON);
    }

    public  boolean doesFavoriteExistAlready(Favorite favorite) {
        boolean result;
        result = mDatabase.getFavoriteDAO().isTripFavorited(favorite.getOrigin(), favorite.getDestination());
        return result;
    }

    public  void addToFavorites(Favorite favorite) {
        mDatabase.getFavoriteDAO().addStation(favorite);
    }

    public  void removeFromFavorites(Favorite favorite) {
        mDatabase.getFavoriteDAO().delete(favorite);
    }
}
