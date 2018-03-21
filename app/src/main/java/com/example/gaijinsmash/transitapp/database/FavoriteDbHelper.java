package com.example.gaijinsmash.transitapp.database;


import android.content.Context;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.bart.Favorite;

import java.util.List;

public class FavoriteDbHelper {

    private FavoriteDatabase mDatabase;
    private Context mContext;

    public FavoriteDbHelper(Context context) {
        this.mContext = context;
        if(mDatabase == null) {
            mDatabase = FavoriteDatabase.getRoomDB(mContext);
        }
    }

    //todo: fix this
    public static void initFavoriteDb(Context context) throws Exception {
        Log.i("init db", "Favorite DB");
        FavoriteDatabase db = FavoriteDatabase.getRoomDB(context);
        if(db.getFavoriteDAO().countFavorites() == 0) {
            List<Favorite> favoriteList = null;

        } else {

        }
    }
}
