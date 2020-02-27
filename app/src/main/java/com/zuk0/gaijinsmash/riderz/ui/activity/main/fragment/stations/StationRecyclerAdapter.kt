package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.stations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station

class StationRecyclerAdapter(private val mStationList: List<Station>?) : RecyclerView.Adapter<StationRecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val address: TextView
        val city: TextView
        val abbr: TextView

        init {
            name = view.findViewById(R.id.stationName_textView)
            address = view.findViewById(R.id.stationAddress_textView)
            city = view.findViewById(R.id.stationCity_textView)
            abbr = view.findViewById(R.id.stationAbbr_textView)
        }
    }

    private var mClickListener: View.OnClickListener? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_row_station, viewGroup, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener { v: View? -> mClickListener!!.onClick(v) }
        return holder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val station = mStationList?.get(i)
        station?.let {
            val name = station.name
            val modifiedName = name!!.replace("International".toRegex(), "Int'l") + " " + viewHolder.name.context.getString(R.string.station)
            viewHolder.name.text = modifiedName
            viewHolder.address.text = station.address
            viewHolder.city.text = station.city
            viewHolder.abbr.text = station.abbr
        }
    }

    override fun getItemCount(): Int {
        return mStationList?.size ?: 0
    }

    fun setClickListener(callback: View.OnClickListener?) {
        mClickListener = callback
    }

}