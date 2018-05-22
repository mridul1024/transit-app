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
}
