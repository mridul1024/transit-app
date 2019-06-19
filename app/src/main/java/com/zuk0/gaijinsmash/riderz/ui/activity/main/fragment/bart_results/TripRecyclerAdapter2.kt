package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bart_results

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Trip
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripBinding
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripContainerBinding
import com.zuk0.gaijinsmash.riderz.databinding.ListRowTripContentBinding
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils

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
        containerBinding  = DataBindingUtil.inflate<ListRowTripContainerBinding>(LayoutInflater.from(parent.context), R.layout.list_row_trip_container, parent,false)
        return ViewHolder(containerBinding)
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = tripList[position]
        Logger.d("Position: $position, Trip: $trip")

        //inject values into container
        if(trip.origTimeDate.isNullOrBlank()) {
            Logger.i("Date: ${trip.origTimeDate}")
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

        //create linear layout and add below parent view (container)
        val layout = LinearLayout(containerBinding.root.context)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layout.layoutParams = params
        containerBinding.tripMainContainer.addView(layout)

        //load legs of  trip
        val numOfLegs = trip.legList.size
        for(i in 0 until numOfLegs) {
            //create a new leg
            val legBinding = DataBindingUtil.inflate<ListRowTripContentBinding>(LayoutInflater.from(holder.binding.root.context), R.layout.list_row_trip_content, null, false)

            //bind color
            BartRoutesUtils.setLineBarByRoute(holder.binding.root.context, trip.legList[i].line, legBinding.tripLegLine)

            //inflate transfer view if need be
            if(i > 0) {
                val stub = legBinding.listRowTransferStub.viewStub
                stub?.inflate()
            }

            //depart time and station
            legBinding.tripLegDepartStation.text = trip.legList[i].origin
            legBinding.tripLegDepartTime.text = trip.legList[i].origTimeMin

            //arrival time and station
            legBinding.tripLegArrivalStation.text = trip.legList[i].destination
            legBinding.tripLegArrivalTime.text = trip.legList[i].destTimeMin

            //add binding to linearlayout
            layout.addView(legBinding.root)
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
