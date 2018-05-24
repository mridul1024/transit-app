package com.zuk0.gaijinsmash.riderz.database;

import android.content.Context;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;

import java.util.List;

public class FavoriteDbHelper {

    public static int getFavoritesCount(Context context) {
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        int count = db.getFavoriteDAO().countFavorites();
        Log.i("fav count", String.valueOf(count));
        db.close();
        return count;
    }

    public static List<Favorite> getFavoritesByPriority(Context context) {
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        List<Favorite> favoritesList= db.getFavoriteDAO().getAllFavoritesByPriority(Favorite.Priority.ON);
        db.close();
        return favoritesList;
    }

    public static boolean doesFavoriteExistAlready(Context context, Favorite favorite) {
        boolean result;
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        result = db.getFavoriteDAO().isTripFavorited(favorite.getOrigin(), favorite.getDestination());
        db.close();
        return result;
    }

    public static void addToFavorites(Context context, Favorite favorite) {
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        db.getFavoriteDAO().addStation(favorite);
        db.close();
    }

    public static void removeFromFavorites(Context context, Favorite favorite) {
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        db.getFavoriteDAO().delete(favorite);
        db.close();
    }
}
