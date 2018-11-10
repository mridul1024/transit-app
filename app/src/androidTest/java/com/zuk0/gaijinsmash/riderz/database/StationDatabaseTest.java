package com.zuk0.gaijinsmash.riderz.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class StationDatabaseTest {
    private StationDao mStationDAO;
    private StationDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, StationDatabase.class).build();
        mStationDAO = mDb.getStationDAO();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeStationToDatabase() throws Exception {
        Station station = new Station();
        station.setId(11);
        station.setName("danville");
        mStationDAO.save(station);
    }

    @Test
    public void readStationFromDatabase() throws IOException {
        Station station = mStationDAO.getStationByID(11);
        assert(station.getName().equalsIgnoreCase("danville"));
    }
}
