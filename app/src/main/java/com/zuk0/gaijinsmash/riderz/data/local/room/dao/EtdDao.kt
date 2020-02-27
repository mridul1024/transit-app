package com.zuk0.gaijinsmash.riderz.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse

@Dao
interface EtdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(etd: EtdXmlResponse?) //@Query("SELECT * from advisories where id = :id")
//EtdXmlResponse getEtdById(int id);
}