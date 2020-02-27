package com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.sql.Timestamp

@Entity(tableName = "advisories")
@Root(name = "root", strict = false)
data class BsaXmlResponse
@JvmOverloads constructor(

    @PrimaryKey
    var id: Int? = 0,

    @ColumnInfo
    var timestamp: Timestamp? = null,

    @ColumnInfo
    @Expose
    @SerializedName("date")
    @param:Element(name = "date", required = false)
    @field:Element(name = "date", required = false)
    var date: String? = null,

    @ColumnInfo
    @Expose
    @SerializedName("time")
    @param:Element(name = "time", required = false)
    @field:Element(name = "time", required = false)
    var time: String? = null,

    @ColumnInfo
    @param:ElementList(inline = true, required = false)
    @field:ElementList(inline = true, required = false, empty = true, entry = "bsa", name = "bs")
    var bsaList: MutableList<Bsa>? = null

)