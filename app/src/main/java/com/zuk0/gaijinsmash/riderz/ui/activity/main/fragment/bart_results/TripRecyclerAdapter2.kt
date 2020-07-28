package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripContainerBinding
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripLegBinding
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils
import com.zuk0.gaijinsmash.riderz.utils.ShareUtils

class TripRecyclerAdapter2(val tripList: List<Trip>) : RecyclerView.Adapter<TripRecyclerAdapter2.ViewHolder>() {

    private lateinit var containerBinding: ListRowTripContainerBinding

    /**************************************************************************************
     *
     **************************************************************************************/

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        containerBinding  = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_trip_container, parent,false)
        return ViewHolder(containerBinding)
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = tripList[position]
        Logger.d("Position: $position, Trip: $trip")

        //inject values into container
        if(!trip.origTimeDate.isNullOrBlank()) {
            Logger.i("Date: ${trip.origTimeDate}")
            containerBinding.tripDate.text = trip.origTimeDate
        }
        if(!trip.tripTime.isNullOrBlank()) {
            containerBinding.tripTotalTime.text = trip.tripTime
            Logger.i("Trip Time: ${trip.tripTime}")
        }
        if(!trip.clipper.isNullOrBlank()) {
            containerBinding.tripItemClipper.text = trip.clipper
            Logger.i("Clipper: ${trip.clipper}")
        }
        if(!trip.fare.isNullOrBlank()) {
            containerBinding.tripItemFare.text = trip.fare
            Logger.i("Cash Fare: ${trip.fare}")
        }

        //load legs of  trip
        val numOfLegs = trip.legList?.size
        for(i in 0 until numOfLegs!!) {
            //create a new leg
            val legBinding = ListRowTripLegBinding.inflate(LayoutInflater.from(holder.binding.root.context))

            //bind color
            BartRoutesUtils.setLineBarByRoute(holder.binding.root.context, trip.legList?.get(i)?.line, legBinding.tripLegLine)

            //inflate transfer view if need be
            if(i > 0) {
                legBinding.listRowTransferStub.visibility = View.VISIBLE
            }

            //depart time and station
            legBinding.tripLegDepartStation.text = trip.legList?.get(i)?.origin
            legBinding.tripLegDepartTime.text = trip.legList?.get(i)?.origTimeMin

            //arrival time and station
            legBinding.tripLegArrivalStation.text = trip.legList?.get(i)?.destination
            legBinding.tripLegArrivalTime.text = trip.legList?.get(i)?.destTimeMin

            //add binding to linearlayout
            containerBinding.tripLegContainer.addView(legBinding.root)
        }

        containerBinding.tripShareButton.setOnClickListener { view ->
            containerBinding.root.rootView.isDrawingCacheEnabled = true
            containerBinding.root.rootView.buildDrawingCache()
            val bitmap = containerBinding.root.rootView.drawingCache
            ShareUtils.shareTrip(view.context, bitmap)
        }
    }

    /**************************************************************************************
     *
     **************************************************************************************/

    inner class ViewHolder(val binding: ListRowTripContainerBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    companion object {
        const val TAG = "TripRecyclerAdapter"
    }
}
