package com.example.gaijinsmash.transitapp.database;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
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

    private class FavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Favorite mFavorite;
        private int mStatus, mColor, mToastNumber;
        private FavoriteDatabase mDatabase;

        public FavoriteAsyncTask(Favorite favorite, int status) {
            this.mFavorite = favorite;
            this.mStatus = status;
            if(mDatabase == null) {
                mDatabase = FavoriteDatabase.getRoomDB(mContext);
            }
        }

        public FavoriteAsyncTask(Favorite favorite, int color, int status) {
            this.mFavorite = favorite;
            this.mColor = color;
            this.mStatus = status;
            if(mDatabase == null) {
                mDatabase = FavoriteDatabase.getRoomDB(mContext);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            switch(mStatus) {
                case 0:
                    Log.i("delete", "was selected");
                    mDatabase.getFavoriteDAO().delete(mFavorite);
                    mToastNumber = 0;
                case 1:
                    //todo: add change color
                case 2:
                    //todo: add priority
                /*
                Favorite reverseFav = new Favorite();
                reverseFav.setOrigin(mFavorite.getDestination());
                reverseFav.setDestination(mFavorite.getOrigin());
                mDatabase.getFavoriteDAO().delete(reverseFav);
                */
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                switch(mToastNumber) {
                    case 0:
                        Toast.makeText(mContext, R.string.deletion_success, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
