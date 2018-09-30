package com.zuk0.gaijinsmash.riderz.ui.fragment.favorite;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class FavoritesViewModel extends AndroidViewModel {

    private enum FavoriteAction { DeleteFavorite, SetAsPriority, DeletePriority }

    private LiveData<List<Favorite>> mFavoritesLiveData;

    FavoritesViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Favorite>> getFavorites() {
        if(mFavoritesLiveData == null) {
            mFavoritesLiveData = FavoriteDatabase.getRoomDB(super.getApplication()).getFavoriteDAO().getAllFavoritesLiveData();
        }
        return mFavoritesLiveData;
    }

    public static void deleteFavorite(Context context, Favorite favorite) {
        FavoriteDatabase.getRoomDB(context).getFavoriteDAO().delete(favorite);
    }

    public static boolean doesPriorityExist(Context context) {
        return FavoriteDatabase.getRoomDB(context).getFavoriteDAO().getAllPriorityFavorites() != null;
    }

    public static void setPriority(Context context, int id) {
        FavoriteDatabase.getRoomDB(context).getFavoriteDAO().setPriorityById(id);
    }

    public static void removePriority(Context context, int id) {
        FavoriteDatabase.getRoomDB(context).getFavoriteDAO().removePriorityById(id);
    }

    public static int getFavoriteCount(Context context) {
        return FavoriteDatabase.getRoomDB(context).getFavoriteDAO().countFavorites();
    }
}
