package com.zuk0.gaijinsmash.riderz.data.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EtdDao {
    @Insert(onConflict = REPLACE)
    void save(EtdXmlResponse etd);

    //@Query("SELECT * from advisories where id = :id")
    //EtdXmlResponse getEtdById(int id);
}
