package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.etd

import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate
import com.zuk0.gaijinsmash.riderz.databinding.ListColEstimateBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.utils.BartRoutesUtils
import com.zuk0.gaijinsmash.riderz.utils.TrainTimer
import java.util.ArrayList

class EstimateViewHolder(val binding: ListColEstimateBinding, val viewModel: HomeViewModel) : RecyclerView.ViewHolder(binding.root) {

    fun bind(estimate: Estimate) {

        //set car length of train
        val carLength = estimate.length.toString() + " car"
        binding.estimateResultCarTv.text = carLength

        //set time remaining
        binding.estimateResultMinutesTv.text = estimate.minutes

        val minutes: String? = estimate.minutes
        if (!minutes.equals("Leaving", ignoreCase = true)) {
            viewModel.timePassedLiveData.observeForever { amountOfTimePassed ->

                if(amountOfTimePassed == TrainTimer.TIMER_FINISHED) {
                    //todo close view
                    /*val newList: MutableList<Estimate> = ArrayList(estimateList)
                    newList.removeAt(position)
                    update(newList)*/
                } else {
                    //create timer object
                    val remainingMinutes = binding.estimateResultMinutesTv.text.toString().toInt()
                    val updatedTime = remainingMinutes - 1
                    val min = binding.root.context.resources.getString(R.string.minutes)
                    val msg = "$updatedTime $min"
                    binding.estimateResultMinutesTv.text = msg
                }
            }
        } else {
            binding.estimateResultMinutesTv.text = "0"
        }

        BartRoutesUtils.setLineBarByColor(binding.background.context, estimate.color, binding.estimateResultTrainIcon)
    }


    companion object {
        private const val TAG = "EstimateViewHolder"
    }
}