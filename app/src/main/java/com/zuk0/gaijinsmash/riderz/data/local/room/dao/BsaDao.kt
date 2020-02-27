package com.zuk0.gaijinsmash.riderz.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import java.sql.Timestamp

@Dao
interface BsaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(bsa: BsaXmlResponse?)

    @Query("SELECT * FROM advisories order by timestamp desc limit 1")
    fun load(): LiveData<BsaXmlResponse?>?

    @Query("SELECT * FROM advisories where timestamp >= :past OR timestamp <= :now ")
    fun bsaExists(past: Timestamp?, now: Timestamp?): Boolean

    @Query("SELECT * From advisories where id = :id")
    fun getBsaById(id: Int): BsaXmlResponse?
}