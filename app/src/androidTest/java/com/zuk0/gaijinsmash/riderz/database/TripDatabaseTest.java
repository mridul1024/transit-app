package com.zuk0.gaijinsmash.riderz.database;

import androidx.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.TripDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class TripDatabaseTest {
    private TripDao mTripDao;
    private TripDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, TripDatabase.class).build();
        mTripDao = mDb.getTripDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeToDb() throws Exception {
        Trip trip = new Trip();
        trip.setId(1);
        trip.setOrigin("danville");
        mTripDao.save(trip);
    }

    @Test
    public void readFromDb() throws Exception {
        Trip trip = mTripDao.getTripById(1);
        assert(trip.getOrigin().equalsIgnoreCase("danville"));
    }
}
