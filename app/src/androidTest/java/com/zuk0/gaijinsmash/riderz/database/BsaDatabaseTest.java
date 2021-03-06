package com.zuk0.gaijinsmash.riderz.database;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import android.content.Context;


import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao;
import com.zuk0.gaijinsmash.riderz.data.local.room.database.BsaDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class BsaDatabaseTest {

    private BsaDao mBsaDao;
    private BsaDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, BsaDatabase.class)
                .allowMainThreadQueries()
                .build();
        mBsaDao = mDb.getBsaDAO();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeToDb() throws Exception {
        BsaXmlResponse bsa = new BsaXmlResponse();
        bsa.setId(1);
        bsa.setTime("10:14");
        mBsaDao.save(bsa);
    }

    @Test
    public void readFromDb() throws Exception {
        BsaXmlResponse bsaXmlResponse = mBsaDao.getBsaById(1);
        assert(bsaXmlResponse.getTime().equals("10:14"));
    }
}
