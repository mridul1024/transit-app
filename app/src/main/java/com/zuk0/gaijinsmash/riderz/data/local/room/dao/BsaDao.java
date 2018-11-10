package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse;

import java.sql.Timestamp;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface BsaDao {
    @Insert(onConflict = REPLACE)
    void save(BsaXmlResponse bsa);

    @Query("SELECT * FROM advisories order by timestamp desc limit 1")
    LiveData<BsaXmlResponse> load();

    @Query("SELECT * FROM advisories where timestamp >= :past OR timestamp <= :now ")
    boolean bsaExists(Timestamp past, Timestamp now);

    @Query("SELECT * From advisories where id = :id")
    BsaXmlResponse getBsaById(int id);
}

