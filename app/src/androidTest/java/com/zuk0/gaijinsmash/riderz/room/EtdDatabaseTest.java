package com.zuk0.gaijinsmash.riderz.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.EtdDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class EtdDatabaseTest {
    private EtdDao mEtdDao;
    private EtdDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, EtdDatabase.class).build();
        mEtdDao = mDb.getEtdDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeEtdToDatabase() throws Exception {
        EtdXmlResponse etd = new EtdXmlResponse();
        etd.setId(1);
        Station station = new Station();
        station.setName("danville");
        etd.setStation(station);
        mEtdDao.save(etd);
    }

    @Test
    public void readEtdFromDatabase() throws IOException {
        //EtdXmlResponse etd = mEtdDao.getEtdById(1);
        //assert(etd.getStation().getName().equals("danville"));
    }

}
