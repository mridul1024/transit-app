package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.etd

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.zuk0.gaijinsmash.riderz.R.string
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.databinding.ListColEstimateBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils
import com.zuk0.gaijinsmash.riderz.utils.TrainTimer.Companion.TIMER_FINISHED

class
EstimateRecyclerAdapter(val estimateList: MutableList<Estimate>, val viewModel: HomeViewModel) : Adapter<EstimateRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListColEstimateBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    //todo one master timer for the longest timeToWait
    //each view holder observes time of the master timer.
    //calculate difference every minute.
    //every 60 seconds, push a new value to observers.

    //private val timers: MutableList<CountDownTimer> = ArrayList()
    private lateinit var binding: ListColEstimateBinding

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return ETD_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListColEstimateBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estimate = estimateList[position]

        //set car length of train
        val carLength = estimate.length.toString()
        holder.binding.estimateResultCarTv.text = carLength

        //set time remaining
        holder.binding.estimateResultMinutesTv.text = estimate.minutes

        val minutes: String? = estimate.minutes
        if (!minutes.equals("Leaving", ignoreCase = true)) {

            viewModel.timePassedLiveData.observeForever { amountOfTimePassed ->
                if(amountOfTimePassed == TIMER_FINISHED || amountOfTimePassed == 0L) {
                    holder.binding.estimateResultMinutesTv.text = "0"
                    //todo close view

                } else {
                    val remainingMinutes = holder.binding.estimateResultMinutesTv.text.toString().toInt()
                    val updatedTime = remainingMinutes - 1
                    val min = holder.binding.root.context.resources.getString(string.minutes)
                    val msg = "$updatedTime $min"
                    holder.binding.estimateResultMinutesTv.text = msg
                }
            }
        } else {
            holder.binding.estimateResultMinutesTv.text = "0"
            //todo close view
        }

        BartRoutesUtils.setLineBarByColor(holder.binding.background.context, estimate.color, holder.binding.estimateResultTrainIcon)
    }


    fun removeEstimate(position: Int) {
        val newList: MutableList<Estimate> = mutableListOf<Estimate>()
        newList.addAll(estimateList)
        newList.removeAt(position)
        update(newList)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Log.i("recycler", "onDetachedFromRecyclerView")
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("onDetachedFromView", "timers cleared")
    }

    override fun getItemCount(): Int {
        return estimateList.size
    }

    fun update(newData: List<Estimate>) {
        val diffResult = DiffUtil.calculateDiff(EstimateDiffCallback(estimateList, newData))
        estimateList.clear()
        estimateList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        private const val TITLE_VIEW_TYPE = 1000
        private const val ETD_VIEW_TYPE = 2000
        private const val FAV_VIEW_TYPE = 2000
    }

}