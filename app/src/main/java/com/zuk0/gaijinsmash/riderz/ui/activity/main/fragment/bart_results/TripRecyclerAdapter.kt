package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripBinding
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils.setLineBarByRoute
import com.zuk0.gaijinsmash.riderz.utils.StationUtils.getStationNameFromAbbr

class TripRecyclerAdapter(private val mContext: Context, private val mTripList: List<Trip>) : RecyclerView.Adapter<TripRecyclerAdapter.ViewHolder>() {
    class ViewHolder(val mDataBinding: ListRowTripBinding) : RecyclerView.ViewHolder(mDataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListRowTripBinding>(LayoutInflater.from(parent.context), R.layout.list_row_trip, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = mTripList[position]
        Log.i("POSITION", position.toString())
        if (trip.origTimeDate != null) holder.mDataBinding.tripDateTextView.text = trip.origTimeDate
        Log.i("origTimeDate", trip.origTimeDate)
        if (trip.tripTime != null) holder.mDataBinding.tripTotalTimeTextView.text = trip.tripTime
        Log.i("tripTime", trip.tripTime)
        if (trip.clipper != null) {
            val `val` = java.lang.Double.valueOf(trip.fare!!) - 0.5
            holder.mDataBinding.tripClipperTextView.text = `val`.toString()
        }
        if (trip.fare != null) holder.mDataBinding.tripFareTextView.text = trip.fare
        initTripLegs(trip, holder, position)
    }

    override fun getItemCount(): Int {
        return mTripList.size
    }

    private fun initTripLegs(trip: Trip, holder: ViewHolder, position: Int) {
        val length = trip.legList?.size ?: 0
        //get the current trip
        if (length > 0) {
            initTextForLeg(LegOrder.FIRST_LEG, holder, mTripList, position)
            setColoredBar(mContext, trip.legList!![0].line, holder, LegOrder.FIRST_LEG)
        }
        if (length > 1) {
            showLegView(LegOrder.SECOND_LEG, holder)
            initTextForLeg(LegOrder.SECOND_LEG, holder, mTripList, position)
            setColoredBar(mContext, trip.legList?.get(1)?.line, holder, LegOrder.SECOND_LEG)
        }
        if (length > 2) {
            showLegView(LegOrder.THIRD_LEG, holder)
            initTextForLeg(LegOrder.THIRD_LEG, holder, mTripList, position)
            setColoredBar(mContext, trip.legList?.get(2)?.line, holder, LegOrder.THIRD_LEG)
        }
    }

    private fun showLegView(leg: LegOrder, holder: ViewHolder) {
        when (leg) {
            LegOrder.SECOND_LEG -> {
                holder.mDataBinding.resultsLeg2.visibility = View.VISIBLE
                holder.mDataBinding.tripTransferLayout1.visibility = View.VISIBLE
            }
            LegOrder.THIRD_LEG -> {
                holder.mDataBinding.resultsLeg3.visibility = View.VISIBLE
                holder.mDataBinding.tripTransferLayout2.visibility = View.VISIBLE
            }
        }
    }

    enum class LegOrder {
        FIRST_LEG, SECOND_LEG, THIRD_LEG
    }

    private fun initTextForLeg(leg: LegOrder, holder: ViewHolder, list: List<Trip>, index: Int) {
        when (leg) {
            LegOrder.FIRST_LEG -> {
                val origin1 = list[index].legList?.get(0)?.origin ?: ""
                val dest1 = list[index].legList?.get(0)?.destination ?: ""
                Log.i("origin1", origin1)
                Log.i("dest1", dest1)
                holder.mDataBinding.tripOriginTextView.text = getStationNameFromAbbr(origin1)
                holder.mDataBinding.tripDestinationTextView.text = getStationNameFromAbbr(dest1)
                val origTimeMin = list[index].legList!![0].origTimeMin
                val destTimeMin = list[index].legList!![0].destTimeMin
                holder.mDataBinding.tripDepartTimeTextView.text = origTimeMin
                holder.mDataBinding.tripArrivalTimeTextView.text = destTimeMin
                Log.i("origTimeMin", origTimeMin)
                Log.i("destTimeMin", destTimeMin)
            }
            LegOrder.SECOND_LEG -> {
                val origin2 = list[index].legList?.get(1)?.origin ?: ""
                val dest2 = list[index].legList?.get(1)?.destination ?: ""
                Log.i("origin2", origin2)
                Log.i("dest2", dest2)
                holder.mDataBinding.tripOriginTextView2.text = getStationNameFromAbbr(origin2)
                holder.mDataBinding.tripDestinationTextView2.text = getStationNameFromAbbr(dest2)
                holder.mDataBinding.tripDepartTimeTextView2.text = list[index].legList!![1].origTimeMin
                holder.mDataBinding.tripArrivalTimeTextView2.text = list[index].legList!![1].destTimeMin
            }
            LegOrder.THIRD_LEG -> {
                val origin3 = list[index].legList?.get(2)?.origin ?: ""
                val dest3 = list[index].legList?.get(2)?.destination ?: ""
                Log.i("origin3", origin3)
                Log.i("dest3", dest3)
                holder.mDataBinding.tripOriginTextView3.text = getStationNameFromAbbr(origin3)
                holder.mDataBinding.tripDestinationTextView3.text = getStationNameFromAbbr(dest3)
                val origTimeMin3 = list[index].legList!![2].origTimeMin
                holder.mDataBinding.tripDepartTimeTextView3.text = origTimeMin3
                holder.mDataBinding.tripArrivalTimeTextView3.text = list[index].legList!![2].destTimeMin
            }
        }
    }

    private fun setColoredBar(context: Context, route: String?, holder: ViewHolder, leg: LegOrder) {
        when (leg) {
            LegOrder.FIRST_LEG -> setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine1)
            LegOrder.SECOND_LEG -> setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine2)
            LegOrder.THIRD_LEG -> setLineBarByRoute(context, route, holder.mDataBinding.tripColoredLine3)
        }
    }

}