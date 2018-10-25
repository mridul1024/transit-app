package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface EtdDao {
    @Insert(onConflict = REPLACE)
    void save(EtdXmlResponse etd);

    //@Query("SELECT * from advisories where id = :id")
    //EtdXmlResponse getEtdById(int id);
}
