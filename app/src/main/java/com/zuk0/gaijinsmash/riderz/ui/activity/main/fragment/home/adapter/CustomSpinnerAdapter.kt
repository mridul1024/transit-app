package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import kotlinx.android.synthetic.main.custom_dropdown_item.view.*
import kotlinx.android.synthetic.main.list_row_station.view.*

class CustomSpinnerAdapter(context: Context, val resourceID: Int, val data: List<Station>) : ArrayAdapter<Station>(context, resourceID, data) {

    private val inflater = LayoutInflater.from(context)

    init {
        Log.d(TAG, "list: $count")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItem(position: Int): Station? {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    fun getPosition(station: Station) : Int {
        return data.indexOf(station)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup) : View {
        val view: View
        val vh: ItemRowHolder
        val item = getItem(position)

        if(convertView == null) {
            view = inflater.inflate(resourceID, parent, false)
            vh = ItemRowHolder(view)
        } else {
            view = convertView
            vh = ItemRowHolder(convertView)
        }

        // set the data
        vh.label.text = item?.name

        return view
    }

    inner class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.custom_dropdown_itemTv) as TextView
    }

    companion object {
        private const val TAG = "CustomSpinnerAdapter"
    }
}