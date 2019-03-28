package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.data.local.constants.RiderzEnums;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class FavoritesViewModel extends AndroidViewModel {

    private enum FavoriteAction { DeleteFavorite, SetAsPriority, DeletePriority }

    FavoritesViewModel(Application application) {
        super(application);
    }

    LiveData<List<Favorite>> getFavorites() {
        return FavoriteDatabase.getRoomDB(getApplication()).getFavoriteDAO().getAllFavoritesLiveData();
    }

    public static void deleteFavorite(Context context, Favorite favorite) {
        new FavoritesTask(context, RiderzEnums.FavoritesAction.DELETE_FAVORITE, favorite).execute();
    }

    public static void addPriority(Context context, Favorite favorite) {
        new FavoritesTask(context, RiderzEnums.FavoritesAction.ADD_PRIORITY, favorite).execute();
    }

    public static void removePriority(Context context, Favorite favorite) {
        new FavoritesTask(context, RiderzEnums.FavoritesAction.DELETE_PRIORITY, favorite).execute();
    }

    //todo: replace with rxjava
    private static class FavoritesTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<Context> mWeakRef;
        private Favorite mFavorite;
        private RiderzEnums.FavoritesAction mAction;

        private FavoritesTask(Context context, RiderzEnums.FavoritesAction action, Favorite favorite) {
            mWeakRef = new WeakReference<>(context);
            this.mFavorite = favorite;
            this.mAction = action;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            switch(mAction){
                case ADD_PRIORITY:
                    if(FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().getPriorityCount() > 0) {
                        return false;
                    }
                    if(FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().getPriorityCount() == 0) {
                        FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().updatePriorityById(mFavorite.getId());
                        return true;
                    }
                    return false;
                case DELETE_PRIORITY:
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().removePriorityById(mFavorite.getId());
                    return true;
                case DELETE_FAVORITE:
                    FavoriteDatabase.getRoomDB(mWeakRef.get()).getFavoriteDAO().delete(mFavorite);
                    return true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result) {
                switch(mAction) {
                    case ADD_PRIORITY:
                        Toast.makeText(mWeakRef.get(), "Set as Priority", Toast.LENGTH_SHORT).show();
                        break;
                    case DELETE_PRIORITY:
                        Toast.makeText(mWeakRef.get(), "Favorite is no longer Priority", Toast.LENGTH_SHORT).show();
                        break;
                    case DELETE_FAVORITE:
                        Toast.makeText(mWeakRef.get(), "Favorite Deleted", Toast.LENGTH_SHORT).show();
                        break;
                }

            } else {
                switch(mAction) {
                    case ADD_PRIORITY:
                        Toast.makeText(mWeakRef.get(), "Only one favorite can be your priority", Toast.LENGTH_LONG).show();
                        break;
                }

            }
        }
    }
}
