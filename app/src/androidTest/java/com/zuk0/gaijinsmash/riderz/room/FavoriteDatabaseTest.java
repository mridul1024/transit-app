package com.zuk0.gaijinsmash.riderz.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class FavoriteDatabaseTest {
    private FavoriteDao mFavoriteDao;
    private FavoriteDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, FavoriteDatabase.class).build();
        mFavoriteDao = mDb.getFavoriteDAO();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeFavoriteToDatabase() throws Exception {
        Favorite favorite = new Favorite();
        favorite.setId(1);
        favorite.setOrigin("danville");
        favorite.setDestination("san ramon");
        mFavoriteDao.save(favorite);
    }

    @Test
    public void readFavoriteFromDatabase() throws IOException {
        Favorite favorite = mFavoriteDao.getFavoriteById(1);
        assert(favorite.getOrigin().equals("danville"));
        assert(favorite.getDestination().equals("san ramon"));
    }
}
